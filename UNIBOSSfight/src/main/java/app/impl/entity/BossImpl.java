package app.impl.entity;

import app.core.component.Transform;
import app.core.component.Weapon;
import app.core.entity.Boss;
import app.core.entity.Bullet;
import app.impl.builder.BehaviourBuilderImpl;
import app.util.AppLogger;
import javafx.geometry.Point2D;
import javafx.scene.Node;

import java.util.List;

/**
 * Implementation of the Boss Interface.
 */
public class BossImpl extends Boss {

    private static final int DEFAULT_RATE_OF_FIRE = 30;
    private transient Weapon weapon;
    private final int rateOfFire;
    private int rateOfFireCounter;

    /**
     * Constructor that initializes a new instance of the Boss.
     *
     * @param startingPos The starting position of the Boss
     * @param height Height of the Boss sprite
     * @param width Width of the Boss sprite
     * @param filename Path of the Boss sprite
     */
    public BossImpl(final Transform startingPos, final int height, final int width, final String filename) {
        super(startingPos, height, width, filename);
        this.setMaxXSpeed(1);
        this.setMaxYSpeed(1);

        rateOfFire = DEFAULT_RATE_OF_FIRE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Weapon getWeapon() {
        return this.weapon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Inputs input) {
        super.update(input);
        this.weapon.updatePosition(getTransform());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node render(final Point2D position) {
        return super.render(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        super.init();

        setBehaviour(new BehaviourBuilderImpl()
                .addJumpOnTop()
                .addStopFromBottom()
                .addStopFromSide()
                .addFollow()
                .addShooting()
                .build());

        getCollider().ifPresent(c -> c.addBehaviour(BulletImpl.class.getName(),
                e -> getHealth().damage(e.getDamage())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shoot(final Point2D target) {
        if (rateOfFireCounter >= rateOfFire) {
            final Bullet newBullet = this.weapon.fire(target);
            newBullet.init();
            addBullet(newBullet);

            rateOfFireCounter = 0;
        } else {
            rateOfFireCounter++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Node> getBulletsNodes() {
        return getBullets().stream().map(e -> e.render(getPosition())).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWeapon(final Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node renderWeapon() {
        // TODO togliere exception generica e print
        try {
            return this.weapon.render(this.getPosition(), this.getDirection(), 0);
        } catch (Exception e) {
            AppLogger.getLogger().warning("ERROR cannot load resource " + e);
        }

        return null;
    }
}