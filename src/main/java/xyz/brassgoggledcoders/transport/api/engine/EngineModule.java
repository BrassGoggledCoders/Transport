package xyz.brassgoggledcoders.transport.api.engine;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.api.TransportObjects;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.BiFunction;

public class EngineModule extends Module<EngineModule> {
    private Item item;

    public EngineModule(BiFunction<EngineModule, IModularEntity, ? extends EngineModuleInstance> engineCreator) {
        super(TransportObjects.ENGINE_TYPE, engineCreator);
    }

    @Override
    @Nonnull
    public Item asItem() {
        if (item == null) {
            item = Optional.ofNullable(this.getRegistryName())
                    .map(name -> new ResourceLocation(name.getNamespace(), name.getPath() + "_engine"))
                    .map(ForgeRegistries.ITEMS::getValue)
                    .orElse(Items.AIR);
        }
        return item;
    }


}
