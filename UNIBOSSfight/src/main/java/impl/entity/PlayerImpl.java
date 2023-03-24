package impl.entity;

import core.component.Collider;
import core.component.Transform;
import core.component.Weapon;
import core.entity.AbstractEntity;
import core.entity.Bullet;
import core.entity.Entity;
import impl.component.AnimatedSpriteRenderer;
import impl.component.ColliderImpl;
import impl.component.SpriteRenderer;
import impl.component.WeaponImpl;
import impl.factory.WeaponFactory;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import util.Acceleration;
import util.Window;

import java.util.ArrayList;
import java.util.List;

public class PlayerImpl extends AbstractEntity {

    private transient double xSpeed = 0;
    private transient double ySpeed = 0;
    private final WeaponFactory weaponFactory = new WeaponFactory();
    private transient final Weapon weapon = weaponFactory.getPlayerWeapon(this.getTransform());
    private transient double rotation;
    private transient final List<Bullet> bullets = new ArrayList<>();
    private transient int coinsCollected = 0;


    public PlayerImpl(final Transform position, final Integer height,
                      final Integer width, final String filename) {
        super(position, height, width,
                new AnimatedSpriteRenderer(height, width, Color.RED, filename));
    }

    @Override
    public boolean isDisplayed(final Point2D position) {
        return true;
    }

    @Override
    public Node render(final Point2D position) {
        try {
            return getRenderer().render(new Point2D(Window.getWidth() / 2,
                    this.getPosition().getY()), this.getDirection(),1,1);
        } catch (Exception e) {
            System.out.println("ERROR cannot load resource " + e);
        }

        return null;
    }

    public Node renderWeapon() {
        try {
            return this.weapon.render(this.getDirection(), (int) this.rotation);
        } catch (Exception e) {
            System.out.println("ERROR cannot load resource " + e);
        }

        return null;
    }

    @Override
    public void update(final Inputs input) {

        switch (input) {
            case LEFT -> {
                this.xSpeed =  Acceleration.accelerate(this.xSpeed, -10, 1);
                setDirection(-1);
            }
            case RIGHT -> {
                this.xSpeed = Acceleration.accelerate(this.xSpeed, 10, 1);
                setDirection(1);
            }
            case SPACE -> {
                if (!isJumping()) {
                    this.ySpeed = -30;
                    getTransform().move(0, -1);
                }
            }
            case EMPTY -> {
                getTransform().move(this.xSpeed, ySpeed);
                this.xSpeed = Acceleration.accelerate(this.xSpeed, 0, 0.5);
                this.ySpeed = this.isJumping()
                        ? Acceleration.accelerate(this.ySpeed, 20, 1) : 0;
                this.bullets.forEach(e -> e.update(Inputs.EMPTY));
                this.removeBullets();
            }
        }

        getTransform().resetGroundLevel();
        getHitbox().update(this.getPosition());
    }

    private boolean isJumping() {
        return this.getPosition().getY() < getTransform().getGroundLevel();
    }

    @Override
    public void initCollider() {
        final var collider = new ColliderImpl();
        collider.addBehaviour(Collider.Entities.TMPENTITY, e -> {
            this.ySpeed = -20;
            this.xSpeed = -20 * this.getDirection();
        });

        collider.addBehaviour(Collider.Entities.WALL, e -> {
            Wall.stop(this, e);
            if (this.getHitbox().getCollisionSideOnY(e.getPosition().getY()) > 0) {
                this.ySpeed = 0;
            }
        });

        collider.addBehaviour(Collider.Entities.PLATFORM, e -> Platform.stop(this, e));

        collider.addBehaviour(Collider.Entities.COIN, e -> {
            this.coinsCollected++;
            e.getHealth().destroy();
        });

        setCollider(collider);
    }

    private double getIntersectionOnX(final Entity e) {
        // side < 0 => left
        // side > 0 => right
        final int side = (int) Math.signum(getPosition().getX() - e.getPosition().getX());

        final double wallSide = side > 0 ? e.getHitbox().getRightSide() : e.getHitbox().getLeftSide();
        final double playerSide = side > 0 ? getHitbox().getLeftSide() : getHitbox().getRightSide();

        return wallSide - playerSide;
    }

    private double getYIntersection(final Entity e) {
        // side < 0 => top
        // side > 0 => bottom
        final int side = (int) Math.signum(getPosition().getY() - e.getPosition().getY());

        final double wallSide = side > 0 ? e.getHitbox().getTopSide() : e.getPosition().getY();
        final double playerSide = side > 0 ? getHitbox().getTopSide() : getPosition().getY();

        return e.getHeight() + (wallSide - playerSide) * side;
    }

    public void rotateWeapon(final Point2D mousePosition) {
        //this.rotation = getDirection()
                //* (mousePosition.getY() / Window.getHeight() * 120 - 55);

        final double dx = (mousePosition.getX() + getPosition().getX() - Window.getWidth() / 2)
                - getPosition().getX();
        final double dy = mousePosition.getY() - getPosition().getY() +30 + 75;
        final double angle = Math.toDegrees(Math.atan2(dy, dx));

        if(angle <= 90 && angle > -90){
            this.setDirection(1);
            this.weapon.setYDirection(1);
        }
        else
        {
            this.setDirection(-1);
            this.weapon.setYDirection(-1);
        }

        this.rotation = angle;
    }

    public void shoot(final Point2D target) {
        final Bullet newBullet = this.weapon.fire(target);
        newBullet.initCollider();
        this.bullets.add(newBullet);
    }

    private void removeBullets() {
        this.bullets.removeIf(e -> !e.isDisplayed(this.getPosition())
                || e.getHealth().isDead());
    }

    public List<Node> getBulletsNodes() {
        return this.bullets.stream().map(e -> e.render(getPosition())).toList();
    }

    public List<Bullet> getBullets() {
        return new ArrayList<>(this.bullets);
    }

    public double getRotation() {
        return this.rotation;
    }
}
