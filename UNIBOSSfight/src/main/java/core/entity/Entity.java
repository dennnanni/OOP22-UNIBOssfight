package core.entity;

import core.component.Collider;
import core.component.Health;
import core.component.Hitbox;
import core.component.Transform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import java.util.Optional;

/**
 * An interface modelling the main concept of the game.
 */
public interface Entity {

    /**
     * Enumeration to manage the input of the entities.
     */
    enum Inputs {
        /**
         * Left input.
         */
        LEFT,
        /**
         * Right input.
         */
        RIGHT,
        /**
         * Space input.
         */
        SPACE,
        /**
         * Empty input.
         */
        EMPTY
    }

    /**
     * This method returns the width of the entity.
     *
     * @return the width of the entity
     */
    int getWidth();

    /**
     * This method returns the height of the entity.
     *
     * @return the height of the entity
     */
    int getHeight();

    /**
     * This method returns the position of the entity.
     *
     * @return the position of the entity
     */
    Point2D getPosition();

    /**
     * This method returns the Hitbox of the entity.
     *
     * @return the Hitbox of the entity
     */
    Hitbox getHitbox();

    /**
     * This method returns the Collider of the entity.
     *
     * @return the Collider of the entity
     */
    Optional<Collider> getCollider();

    /**
     * This method returns the Health of the entity.
     *
     * @return the Health of the entity
     */
    Health getHealth();

    /**
     * This method returns the Transform of the entity.
     *
     * @return the Transform of the entity
     */
    Transform getTransform();

    /**
     * This method checks if the entity is displayed in the game window.
     *
     * @param position position of the player
     * @return true if the entity is close enough to the player in order
     * to be rendered inside the game window, false otherwise.
     */
    boolean isDisplayed(Point2D position);

    boolean isUpdated(Point2D position);

    /**
     * This method is used to render the entity.
     *
     * @param position of the entity
     * @return the Node generated by the renderer which will be given as input to the Scene
     */
    Node render(Point2D position);

    /**
     * Takes as input an element of the Inputs enum and, from that,
     * the class will do the update.
     *
     * @param input an element of the enum
     */
    void update(Inputs input);

    /**
     * Manages the collision of the entity.
     *
     * @param e the entity colliding
     */
    void manageCollision(Entity e);

    /**
     * Initialise the collider of the entity.
     */
    void initCollider();



}
