package xyz.brassgoggledcoders.transport.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.content.*;

import java.util.function.Supplier;

public class TransportLanguageProvider extends LanguageProvider {
    public TransportLanguageProvider(DataGenerator gen) {
        super(gen, Transport.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        //region Blocks
        this.addBlock(TransportBlocks.DIAMOND_CROSSING_RAIL, "Diamond Crossing Rail");
        this.addBlock(TransportBlocks.SCAFFOLDING_RAIL, "Scaffolding Rail");
        this.addBlock(TransportBlocks.HOLDING_RAIL, "Holding Rail");
        this.addBlock(TransportBlocks.ELEVATOR_SWITCH_RAIL, "Elevator Switch Rail");
        this.addBlock(TransportBlocks.ELEVATOR_SWITCH_SUPPORT, "Elevator Switch Support");
        this.addBlock(TransportBlocks.ITEM_LOADER, "Item Loader");
        this.addBlock(TransportBlocks.ENERGY_LOADER, "Energy Loader");
        this.addBlock(TransportBlocks.FLUID_LOADER, "Fluid Loader");
        this.addBlock(TransportBlocks.SCAFFOLDING_SLAB_BLOCK, "Scaffolding Slab");
        this.addBlock(TransportBlocks.SWITCH_RAIL, "Switch Rail");
        this.addBlock(TransportBlocks.WYE_SWITCH_RAIL, "Wye Switch Rail");
        this.addBlock(TransportBlocks.BUMPER_RAIL, "Bumper Rail");
        //endregion

        //region Cargo
        this.add(TransportCargoModules.ITEM, "Inventory");
        this.add(TransportCargoModules.FLUID, "Fluid Tank");
        this.add(TransportCargoModules.ENERGY, "Energy Storage");
        //endregion

        //region Entity
        this.add(TransportEntities.CARGO_MINECART.get(), "Minecart");
        this.add(TransportEntities.CARGO_MINECART_ITEM.get(), "Minecart");
        //endregion

        //region Engine
        this.add(TransportEngineModules.CREATIVE, "Creative Engine");
        this.add(TransportEngineModules.SOLID_FUEL, "Solid Fuel Engine");
        this.add(TransportEngineModules.BOOSTER, "Booster Engine");
        //endregion

        //region Item
        this.add(TransportItems.RAIL_BREAKER.get(), "Rail Breaker");
        //endregion

        //region Text
        this.add("itemGroup.transport", "Transport");
        this.add("text.transport.with", "%s With %s");
        this.add("guide.transport.name", "Advanced Transport");
        this.add("guide.transport.landing_text", "Importing the Transportation of Goods");
        //endregion
    }

    public void add(Supplier<? extends Module<?>> registryEntry, String name) {
        this.add(registryEntry.get(), name);
    }

    public void add(Module<?> registryEntry, String name) {
        this.add(registryEntry.getTranslationKey(), name);
    }
}
