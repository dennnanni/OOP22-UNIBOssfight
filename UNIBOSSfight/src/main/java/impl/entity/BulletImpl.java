package impl.entity;

import core.component.Hitbox;
import core.entity.Bullet;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Point2D;

public class BulletImpl implements Bullet {

    int damage;
    Point2D position;
    Point2D target;
    Point2D trajectory;
    Hitbox hitbox;

    public BulletImpl(Point2D startingPos, Point2D target, int damage, Hitbox hitbox){
        this.position = startingPos;
        this.target = target;
        this.damage = damage;
        this.hitbox = hitbox;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public boolean isDisplayed() {
        //TODO
        return false;
    }

    @Override
    public void render(GraphicsContext gc) {
        //TODO
    }

    @Override
    public void update() {
        //TODO
    }

    @Override
    public javafx.geometry.Point2D getPosition() {
        return this.position;
    }

    @Override
    public Hitbox getHitbox() {
        return this.hitbox;
    }
}