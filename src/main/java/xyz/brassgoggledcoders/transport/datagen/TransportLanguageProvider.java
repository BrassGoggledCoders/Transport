package xyz.brassgoggledcoders.transport.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
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
        this.addCargo(TransportCargoModules.ITEM, "Inventory");
        this.addCargo(TransportCargoModules.FLUID, "Fluid Tank");
        this.addCargo(TransportCargoModules.ENERGY, "Energy Storage");
        //endregion

        //region Entity
        this.add(TransportEntities.CARGO_MINECART.get(), "Minecart");
        this.add(TransportEntities.CARGO_MINECART_ITEM.get(), "Minecart");
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

    public void addCargo(Supplier<? extends Cargo> cargo, String name) {
        this.add(cargo.get(), name);
    }

    public void add(Cargo cargo, String name) {
        this.add(cargo.getTranslationKey(), name);
    }
}
