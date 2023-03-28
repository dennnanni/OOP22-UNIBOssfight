package app.impl.entity;

import app.core.component.Renderer;
import app.core.component.Transform;
import app.core.entity.AbstractEntity;
import app.impl.component.SpriteRenderer;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * This class models a coin, which contributes to computing the final
 * score of the player by adding points every time a coin is collected.
 */
public class Coin extends AbstractEntity {
    private final int value;
    private boolean collected;

    /**
     * Creates a new instance of the class Coin.
     *
     * @param position the position of the coin
     * @param height the height of the coin
     * @param width the width of the coin
     * @param filename the name of the file containing the sprite for the renderer
     */
    public Coin(final Transform position, final int height,
                final int width, final String filename) {
        super(position, height, width,
                new SpriteRenderer(height, width, Color.YELLOW, filename));
        this.value = 1;
        this.collected = false;
    }

    /**
     * Creates a new instance of the class Coin.
     *
     * @param position the position of the coin
     * @param height the height of the coin
     * @param width the width of the coin
     * @param renderer the renderer
     */
    public Coin(final Transform position, final int height,
                final int width, final Renderer renderer) {
        super(position, height, width, renderer);
        this.value = 1;
    }

    /**
     * This method is used to collect the coin and get its value,
     * that will be added to the player's score.
     *
     * @return the value of the coin
     */
    public int collect() {
        if (!this.collected) {
            this.collected = true;
            return this.value;
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDisplayed(final Point2D position) {
        if (collected) {
            return false;
        }
        return super.isDisplayed(position);
    }

}
