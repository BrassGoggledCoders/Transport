package xyz.brassgoggledcoders.transport.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class LazyEntity<T extends Entity> implements Function<ServerLevel, T> {
    private final UUID uuid;
    private final Class<T> entityClass;

    private WeakReference<T> weakReference;

    public <U extends T> LazyEntity(U entity, Class<T> entityClass) {
        this.uuid = entity.getUUID();
        this.entityClass = entityClass;
        this.weakReference = new WeakReference<>(entity);
    }

    public LazyEntity(UUID uuid, Class<T> entityClass) {
        this.uuid = uuid;
        this.entityClass = entityClass;
        this.weakReference = null;
    }

    @Override
    @Nullable
    public T apply(ServerLevel serverLevel) {
        T actualEntity = weakReference != null ? weakReference.get() : null;
        if (actualEntity == null) {
            Entity entity = serverLevel.getEntity(uuid);
            if (entityClass.isInstance(entity)) {
                actualEntity = entityClass.cast(entity);
                this.weakReference = new WeakReference<>(actualEntity);
            }
        }
        return actualEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LazyEntity<?> that = (LazyEntity<?>) o;
        return uuid.equals(that.uuid) && entityClass.equals(that.entityClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, entityClass);
    }
}
