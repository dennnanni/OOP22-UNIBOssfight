package app.impl.component;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This class is used to render an animation that loops over time.
 */
public class LoopSpriteRenderer extends SpriteRenderer {

    private static final int ANIMATION_DURATION = 24;

    private transient List<ImageView> preRenderedSprites;
    private transient int animationLength;
    private transient int cont;
    private transient int contDelay;
    private transient int maxDelay;

    /**
     * Creates a new instance of the class SpriteRenderer.
     *
     * @param height   the height of the entity
     * @param width    the width of the entity
     * @param color    the color which will be given to the sprite
     * @param filename the name of the file containing the sprite
     *                 to be used for rendering
     */
    public LoopSpriteRenderer(final int height, final int width, final Color color, final String filename) {
        super(height, width, color, filename);
    }

    /**
     * @param position   the position of the entity
     * @param xDirection the direction on the x-axis of the entity
     * @param yDirection the direction on the y-axis of the entity
     * @param rotation   the rotation of the entity
     * @return the current sprite to render of the animation
     */
    @Override
    public Node render(final Point2D position, final int xDirection, final int yDirection, final double rotation) {
        this.setPrerendered(this.preRenderedSprites.get(cont % this.animationLength));

        if (this.contDelay % this.maxDelay == 0) {
            this.cont++;
        }

        this.contDelay++;

        return super.render(position, xDirection, yDirection, rotation);
    }

    /**
     *  Initialize the sprites of the loop and sets the animation length.
     */
    @Override
    public void init() {
        final File directory = new File("assets/" + this.getFilename());
        final String pathname = "assets/" + this.getFilename() +  "/" + this.getFilename();

        this.animationLength = Objects.requireNonNull(directory.list()).length;
        this.maxDelay = LoopSpriteRenderer.ANIMATION_DURATION / this.animationLength;

        this.preRenderedSprites = IntStream.iterate(1, e -> e + 1)
                .limit(this.animationLength)
                .mapToObj(n -> {
                    try {
                        return new Image(
                                new FileInputStream(pathname + n + ".png"),
                                getWidth(), getHeight(),
                                false,
                                true);
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .map(this::createImageView)
                .toList();
    }

    /**
     * sets the list of pre rendered sprites.
     *
     * @param preRenderedSprites the list to set
     */
    protected void setPreRenderedSprites(final List<ImageView> preRenderedSprites) {
        this.preRenderedSprites = preRenderedSprites;
    }

    /**
     * Sets the animation duration.
     *
     * @param animationLength the animation to set
     */
    public void setAnimationLength(final int animationLength) {
        this.animationLength = animationLength;
        this.maxDelay = LoopSpriteRenderer.ANIMATION_DURATION / this.animationLength;
    }

    /**
     * Restarts the animation.
     */
    protected void resetAnimation() {
        this.cont = 0;
        this.contDelay = 0;
    }
}
