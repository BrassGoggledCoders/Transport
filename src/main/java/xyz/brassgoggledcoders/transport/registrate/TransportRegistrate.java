package xyz.brassgoggledcoders.transport.registrate;

import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class TransportRegistrate extends AbstractRegistrate<TransportRegistrate> {
    public static TransportRegistrate create(String modid) {
        Preconditions.checkNotNull(FMLJavaModLoadingContext.get(), "Registrate initialized too early!");
        return new TransportRegistrate(modid).registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private TransportRegistrate(String modid) {
        super(modid);
    }

    public <T extends HullType, I extends Item> HullTypeBuilder<T, I, TransportRegistrate> hullType(Function<NonNullSupplier<I>, T> hullTypeCreator) {
        return this.hullType(this, hullTypeCreator);
    }

    public <T extends HullType, I extends Item, P> HullTypeBuilder<T, I, P> hullType(P parent, Function<NonNullSupplier<I>, T> hullTypeCreator) {
        return this.entry((name, builderCallback) ->
                HullTypeBuilder.create(this, parent, name, builderCallback, hullTypeCreator));
    }

    public ModuleSlotBuilder<ModuleSlot, TransportRegistrate> moduleSlot(BiPredicate<IModularEntity, Module<?>> isValid) {
        return this.moduleSlot(() -> new ModuleSlot(isValid));
    }

    public <M extends ModuleSlot> ModuleSlotBuilder<M, TransportRegistrate> moduleSlot(NonNullSupplier<M> moduleSlotCreator) {
        return this.entry((name, builderCallback) -> new ModuleSlotBuilder<>(this, this, this.currentName(),
                builderCallback, moduleSlotCreator));
    }

    public <M extends ModuleSlot, P> ModuleSlotBuilder<M, P> moduleSlot(P parent, NonNullSupplier<M> moduleSlotCreator) {
        return this.entry((name, builderCallback) -> new ModuleSlotBuilder<>(this, parent, this.currentName(),
                builderCallback, moduleSlotCreator));
    }

    public ModuleTypeBuilder<ModuleType, TransportRegistrate> moduleType(Function<ResourceLocation, Module<?>> loadValue,
                                                                         NonNullSupplier<Collection<Module<?>>> getValues) {
        return this.moduleType(() -> new ModuleType(loadValue, getValues));
    }

    public <M extends ModuleType> ModuleTypeBuilder<M, TransportRegistrate> moduleType(NonNullSupplier<M> moduleTypeBuilder) {
        return this.entry((name, builderCallback) -> new ModuleTypeBuilder<>(this, this, this.currentName(),
                builderCallback, moduleTypeBuilder));
    }

    public <M extends ModuleType, P> ModuleTypeBuilder<M, P> moduleType(P parent, NonNullSupplier<M> moduleTypeBuilder) {
        return this.entry((name, builderCallback) -> new ModuleTypeBuilder<>(this, parent, this.currentName(),
                builderCallback, moduleTypeBuilder));
    }
}
