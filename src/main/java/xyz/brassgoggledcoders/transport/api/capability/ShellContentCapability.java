package xyz.brassgoggledcoders.transport.api.capability;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class ShellContentCapability<T, C> extends BaseCapability<T, C> {
    private static final CapabilityRegistry<ShellContentCapability<?, ?>> registry = new CapabilityRegistry<>(
            ShellContentCapability::new
    );

    private final Map<Codec<? extends IShellContentCreator<?>>, List<ICapabilityProvider<ShellContent, C, T>>> providers;

    private EntityCapability<T, C> entityCapability;

    private ShellContentCapability(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        super(name, typeClass, contextClass);
        this.providers = new IdentityHashMap<>();
        this.entityCapability = null;
    }

    public void setEntityCapability(EntityCapability<T, C> entityCapability) {
        this.entityCapability = entityCapability;
    }

    public EntityCapability<T, C> getEntityCapability() {
        return this.entityCapability;
    }

    public <E extends Entity & IShell> void registerEntityCapability(RegisterCapabilitiesEvent event, EntityType<E> eEntityType) {
        event.registerEntity(
                this.getEntityCapability(),
                eEntityType,
                (entity, context) -> entity.getContent()
                        .getCapability(this, context)
        );
    }

    @ApiStatus.Internal
    @Nullable
    public T getCapability(ShellContent shellContent, C context) {
        Codec<? extends IShellContentCreator<?>> codec = shellContent.getCreatorInfo()
                .contentCreator()
                .getCodec();
        for (var provider : providers.getOrDefault(codec, List.of())) {
            var ret = provider.getCapability(shellContent, context);
            if (ret != null)
                return ret;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <SC extends ShellContent> void addProvider(
            Codec<? extends IShellContentCreator<? extends SC>> codec,
            ICapabilityProvider<SC, C, T> capabilityProvider
    ) {
        this.providers.computeIfAbsent(codec, c -> new ArrayList<>())
                .add((ICapabilityProvider<ShellContent, C, T>) capabilityProvider);
    }

    public static synchronized List<ShellContentCapability<?, ?>> getAll() {
        return registry.getAll();
    }

    @SuppressWarnings("unchecked")
    public static <T, C> ShellContentCapability<T, C> create(ResourceLocation name, Class<T> typeClass,
                                                             Class<C> contextClass, @Nullable EntityCapability<T, C> entityCapability) {
        ShellContentCapability<T, C> shellContentCapability = (ShellContentCapability<T, C>) registry.create(name, typeClass, contextClass);
        shellContentCapability.setEntityCapability(entityCapability);
        return shellContentCapability;
    }

    public static <T> ShellContentCapability<T, Void> createVoid(ResourceLocation name, Class<T> typeClass,
                                                                 @Nullable EntityCapability<T, Void> entityCapability) {
        return create(name, typeClass, void.class, entityCapability);
    }

    public static <T> ShellContentCapability<T, @Nullable Direction> createSided(ResourceLocation name, Class<T> typeClass,
                                                                                 @Nullable EntityCapability<T, @Nullable Direction> entityCapability) {
        return create(name, typeClass, Direction.class, entityCapability);
    }
}
