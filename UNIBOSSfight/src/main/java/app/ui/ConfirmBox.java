package app.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class ConfirmBox {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    private static boolean answer;

    private ConfirmBox() {

    }

    public static boolean display(final String message) {
        final Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setWidth(WIDTH);
        window.setHeight(HEIGHT);

        final Label label = new Label();
        label.setText(message);

        final Button yesButton = new Button("Yes");
        final Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            System.exit(0);
        });

        final VBox layout = new VBox(10);
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);

        final Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

        return answer;
    }

}