package impl.entity;

import core.component.Hitbox;
import core.entity.PassiveEntity;
import javafx.scene.canvas.GraphicsContext;
import util.Vector2d;

public class Coin implements PassiveEntity {
    private final boolean isHarmful;
    private final int value;
    private Vector2d coinPosition;
    private Hitbox coinHitbox;

    public Coin() {
        this.isHarmful = false;
        this.value = 1;
    }

    @Override
    public boolean isDisplayed() {
        return false;
    }

    @Override
    public void render(GraphicsContext gc) {

    }

    @Override
    public void update() {

    }

    @Override
    public Vector2d getPosition() {
        return this.coinPosition;
    }

    @Override
    public Hitbox getHitbox() {
        return this.coinHitbox;
    }

    @Override
    public boolean isHarmuful() {
        return this.isHarmful;
    }

    public int getValue() {
        return this.value;
    }
}
