package xyz.brassgoggledcoders.transport.immersiveengineering.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.transport.immersiveengineering.TransportImmersiveEngineering;
import xyz.brassgoggledcoders.transport.immersiveengineering.compat.ImmersiveEngineeringHullTypes;

public class TransportIEEnglishLanguageProvider extends LanguageProvider {
    public TransportIEEnglishLanguageProvider(DataGenerator gen) {
        super(gen, TransportImmersiveEngineering.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ImmersiveEngineeringHullTypes.TREATED_WOOD_BOAT.get(), "Treated Wood Boat");
    }
}
