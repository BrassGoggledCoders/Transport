package xyz.brassgoggledcoders.transport.immersiveengineering.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import xyz.brassgoggledcoders.transport.immersiveengineering.TransportImmersiveEngineering;
import xyz.brassgoggledcoders.transport.immersiveengineering.compat.ImmersiveEngineeringHullTypes;

import java.util.Optional;
import java.util.function.Supplier;

public class TransportIEItemModels extends ItemModelProvider {
    public TransportIEItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TransportImmersiveEngineering.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.genericItem(ImmersiveEngineeringHullTypes.TREATED_WOOD_BOAT);
    }

    protected ItemModelBuilder genericItem(Supplier<? extends Item> item) {
        return Optional.ofNullable(item.get())
                .map(Item::getRegistryName)
                .map(ResourceLocation::getPath)
                .map(this::genericItem)
                .orElseThrow(() -> new IllegalStateException("Tried to generate model for invalid Item"));
    }

    protected ItemModelBuilder genericItem(String path) {
        return this.singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
    }
}
