package impl.component;

import core.component.Collider;
import core.entity.Entity;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * {@inheritDoc}
 */
public class ColliderImpl implements Collider {

    private final Map<Entities, Consumer<Entity>> behaviours;

    /**
     * Crea una nuova istanza del gestore delle collisioni.
     */
    public ColliderImpl() {
        this.behaviours = new EnumMap<>(Entities.class);
    }

    /**
     * {@inheritDoc}
     */
    public void manageCollision(final Entity e) {
        this.behaviours.entrySet().stream().filter(b -> b.getKey().equals(e))
                .findFirst().ifPresent(b -> b.getValue().accept(e));
    }

    /**
     * {@inheritDoc}
     */
    public void addBehaviour(final Entities key, final Consumer<Entity> value) {
        this.behaviours.putIfAbsent(key, value);
    }
}
