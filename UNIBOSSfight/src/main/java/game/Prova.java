package game;

import core.entity.Entity;
import impl.component.PatternRender;
import impl.component.TransformImpl;
import impl.entity.Wall;
import impl.level.LevelImpl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui.ConfirmBox;
import util.DataManager;
import util.Window;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Prova extends Application {

    private static final int FRAME_RATE = 60;
    private static final double FRAME_DURATION = 1000 / FRAME_RATE;
    private static final int MIN_WINDOW_HEIGHT = 600;
    private static final int MIN_WINDOW_WIDTH = 800;

    private final LevelImpl currentLevel = new DataManager().loadLevel();
    private Group root = new Group();
    private Scene currentScene;
    private InputManager inputManager;
    private Image image;
    private Paint imagePattern;

    @Override
    public void start(final Stage stage) {

        stage.setTitle("UNIBOssfight");

        stage.setOnCloseRequest(e -> {
            e.consume();
            saveState();
        });

        final Screen screen = Screen.getPrimary();
        final Rectangle2D bounds = screen.getVisualBounds();

        FileInputStream input = null;
        try {
            input = new FileInputStream("assets/ground/ground.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.image = new Image(input);

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setMinHeight(MIN_WINDOW_HEIGHT);
        stage.setMinWidth(MIN_WINDOW_WIDTH);

        this.root = new Group();

        //Creating a scene object
        currentScene = new Scene(root);

        this.inputManager = new InputManager(currentScene);

        this.currentScene.heightProperty().addListener(
                (observable, oldValue, newValue) -> Window.setHeight(currentScene.getHeight() - this.image.getHeight())
        );

        this.currentScene.widthProperty().addListener(
                (observable, oldValue, newValue) -> Window.setWidth(currentScene.getWidth())
        );


        currentScene.setOnMouseClicked(e -> this.currentLevel.playerShoot(new Point2D(e.getX(), e.getY())));

        //Adding scene to the stage
        stage.setScene(currentScene);

        //Displaying the contents of the stage
        stage.show();

        final Timeline tl = new Timeline(new KeyFrame(Duration.millis(FRAME_DURATION), e -> {
            run();
        }));
        tl.setCycleCount(Animation.INDEFINITE);

        tl.play();
        this.currentLevel.init();
    }

    private void inputPoll() {
    }

    private void update() {
        this.currentLevel.updateEntities();
        if (this.inputManager.isSpacePressed) {
            this.currentLevel.updatePlayer(Entity.Inputs.SPACE);
        }
        if (this.inputManager.isDPressed) {
            this.currentLevel.updatePlayer(Entity.Inputs.RIGHT);
        }
        if (this.inputManager.isAPressed) {
            this.currentLevel.updatePlayer(Entity.Inputs.LEFT);
        }
        this.currentLevel.updatePlayer(Entity.Inputs.EMPTY);

        this.currentLevel.collision();
    }

    private void render() {
        this.root.getChildren().clear();
        root.getChildren().add(this.currentLevel.renderPlayer());
        root.getChildren().add(this.currentLevel.renderWeapon());
        this.currentLevel.renderEntities().forEach(e -> root.getChildren().add(e));

        // create a Rectangle
        final Rectangle rect = new Rectangle(0, Window.getHeight(), Window.getWidth(), image.getHeight());

        // set fill for rectangle
        this.imagePattern = new ImagePattern(
                image,
                -this.currentLevel.getPlayerPosition().getX(),
                Window.getHeight() - image.getHeight(),
                image.getWidth(), image.getHeight(),
                false
        );

        rect.setFill(this.imagePattern);

        root.getChildren().add(rect);
    }

    // TODO creare copyOf del level
    public LevelImpl getCurrentLevel() {
        return this.currentLevel;
    }

    private void run() {
        inputPoll();
        update();
        render();
    }

    private class InputManager {
        private final Scene scene;
        private boolean isAPressed = false;
        private boolean isDPressed = false;
        private boolean isSpacePressed = false;


        public InputManager(final Scene scene) {
            this.scene = scene;

            this.scene.setOnKeyPressed(e -> {
                switch (e.getCode()) {
                    case A:
                        this.isAPressed = true;
                        break;
                    case D:
                        this.isDPressed = true;
                        break;
                    case SPACE:
                        this.isSpacePressed = true;
                        break;
                }
            });

            this.scene.setOnMouseMoved(e -> currentLevel.rotatePlayerWeapon(new Point2D(e.getX(), e.getY())));

            this.scene.setOnKeyReleased(e -> {
                switch (e.getCode()) {
                    case A:
                        this.isAPressed = false;
                        break;
                    case D:
                        this.isDPressed = false;
                        break;
                    case SPACE:
                        this.isSpacePressed = false;
                        break;
                }
            });
        }
    }

    @Override
    public void stop() throws Exception {
        new DataManager().serializeLevel(this.currentLevel);
        super.stop();
    }

     private void saveState() {
        boolean answer = ConfirmBox.display("Do you want to save the state?");
        if (answer) {
            try {
                this.stop();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
