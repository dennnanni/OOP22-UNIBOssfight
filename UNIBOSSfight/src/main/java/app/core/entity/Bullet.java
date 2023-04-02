package app.core.entity;

import app.core.component.Collider;
import app.core.component.Renderer;
import app.core.component.Transform;
import app.impl.component.ColliderImpl;
import app.impl.entity.Platform;
import app.impl.entity.Wall;
import javafx.geometry.Point2D;
import app.util.Window;

/**
 * This class models the bullet shot by the weapons
 * of the game, which can cause damage, has a
 * speed and shifts along a vector.
 */
public abstract class Bullet extends AbstractEntity {

    private final double xShift;
    private final double yShift;

    /**
     * Creates a new instance of the class Bullet.
     *
     * @param startingPos the starting position of the bullet
     * @param height the height of the bullet
     * @param width the width of the bullet
     * @param renderer the renderer of the bullet
     * @param damage the damage caused by bullet
     * @param target the spot giving the trajectory
     * @param speed the speed of the bullet
     */
    public Bullet(final Transform startingPos, final int height, final int width,
                  final Renderer renderer, final int damage, final Point2D target,
                  final int speed) {

        super(startingPos, height, width, renderer);
        this.setDamage(damage);

        // Finding vector angle
        final double dx = (target.getX() + getPosition().getX() - Window.getWidth() / 2)
                - getPosition().getX();
        final double dy = Window.getHeight() - target.getY() - getPosition().getY();
        final double angle = -Math.atan2(dy, dx); // Adjusted to new reference system
        this.xShift = speed * Math.cos(angle);
        this.yShift = -speed * Math.sin(angle);

        this.getTransform().setRotation(Math.toDegrees(angle));
    }

    /**
     * Updates Bullet position and hitbox.
     */
    public void update() {
        getTransform().move(xShift, yShift);
        this.getHitbox().update(this.getPosition());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        super.init();

        final Collider collider = new ColliderImpl();

        collider.addBehaviour(Wall.class.getName(), e -> this.getHealth().destroy());

        collider.addBehaviour(Platform.class.getName(), e -> {
            if (this.getHitbox().getCollisionSideOnY(e.getPosition().getY()) > 0) {
                this.getHealth().destroy();
            }
        });

        setCollider(collider);
    }
}
