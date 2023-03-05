package core.component;


import core.entity.*;
import impl.entity.*;

import java.util.function.Consumer;

/**
 * Interfaccia che gestisce i comportamenti delle entità alle collisioni con
 * le varie entità.
 */
public interface Collider {

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
         * Verifica se il parametro è dello stesso tipo runtime di quello
         * associato al valore dell'enum.
         * @param e entità da confrontare
         * @param <T> sottotipo di entity
         * @return true se il parametro è dello stesso tipo, false altrimenti
         */
        public <T extends Entity> boolean equals(T e) {
            return e.getClass().equals(type);
        }
    }

    /**
     * Esegue la procedura di gestione della collisione.
     * @param e entità con cui il chiamante collide
     */
    void manageCollision(Entity e);

    /**
     * Associa un comportamento ad una determinata entità.
     * @param key enum dell'entità
     * @param value comportamento
     */
    void addBehaviour(Entities key, Consumer<Entity> value);
}
