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
