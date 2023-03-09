package core.component;

import core.entity.Entity;
import impl.entity.BossImpl;
import impl.entity.BulletImpl;
import impl.entity.EnemyImpl;
import impl.entity.Flame;
import impl.entity.Platform;
import impl.entity.PlayerImpl;
import impl.entity.Spine;
import impl.entity.TmpEntityImpl;
import java.util.function.Consumer;

/**
 * An interface modelling the behaviours of the entities on collisions.
 */
public interface Collider extends Component {

    /**
     * Enum con tutte le entità possibili.
     */
    enum Entities {
        // TODO valutare se mettere questa enum in un file a parte
        TMPENTITY(TmpEntityImpl.class),
        PLAYER(PlayerImpl.class),
        ENEMY(EnemyImpl.class),
        BOSS(BossImpl.class),
        PLATFORM(Platform.class),
        FLAME(Flame.class),
        SPINE(Spine.class),
        BULLET(BulletImpl.class);

        private final Class<? extends Entity> type;

        Entities(final Class<? extends Entity> type) {
            this.type = type;
        }

        /**
         * Verifies if the parameter is the same runtime type of the one associated
         * to the value of the enumeration.
         * @param e comparing entity
         * @param <T> subtype of Entity
         * @return true if the type is the same, false if it's not
         */
        public <T extends Entity> boolean equals(final T e) {
            return e.getClass().equals(type);
        }
    }

    /**
     * Manages the collision executing the procedure associated to the colliding entity.
     * @param e entity with which the caller collides
     */
    void manageCollision(Entity e);

    /**
     * Associates a behaviour to a specific entity.
     * @param key enum value of the entity
     * @param value behaviour
     */
    void addBehaviour(Entities key, Consumer<Entity> value);
}
