package xyz.brassgoggledcoders.transport.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.content.TransportModuleSlots;
import xyz.brassgoggledcoders.transport.content.*;

import java.util.function.Supplier;

public class TransportLanguageProvider extends LanguageProvider {
    public TransportLanguageProvider(DataGenerator gen) {
        super(gen, Transport.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        //region Blocks
        this.addBlock(TransportBlocks.MODULE_CONFIGURATOR, "Module Configurator");
        //endregion

        //region Cargo
        this.add(TransportCargoModules.ITEM, "Inventory");
        this.add(TransportCargoModules.FLUID, "Fluid Tank");
        this.add(TransportCargoModules.ENERGY, "Energy Storage");
        //endregion

        //region Engine
        this.add(TransportEngineModules.CREATIVE, "Creative Engine");
        this.add(TransportEngineModules.SOLID_FUEL, "Solid Fuel Engine");
        this.add(TransportEngineModules.BOOSTER, "Booster Engine");
        //endregion

        //region ModuleSlots
        this.add(TransportModuleSlots.CARGO.get(), "Cargo");
        this.add(TransportModuleSlots.BACK.get(), "Back");
        this.add(TransportModuleSlots.NONE.get(), "Not a");
        this.add("text.transport.module_slot", "%s Module Slot");
        //endregion

        //region HullType
        this.addHullType(TransportHullTypes.OAK, "Oak Boat");
        this.add("text.transport.hull_type", "Hull Type: %s");
        //

        //region Text
        this.add("itemGroup.transport", "Transport");
        this.add("text.transport.with", "%s With %s");
        this.add("text.transport.installed_modules", "Installed Modules");
        this.add("text.transport.installed_module", " * %s - %s");
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

    public void add(ModuleSlot moduleSlot, String name) {
        this.add(moduleSlot.getTranslationKey(), name);
    }

    public void addHullType(Supplier<? extends HullType> hullType, String name) {
        this.add(hullType.get().getTranslationKey(), name);
    }
}
