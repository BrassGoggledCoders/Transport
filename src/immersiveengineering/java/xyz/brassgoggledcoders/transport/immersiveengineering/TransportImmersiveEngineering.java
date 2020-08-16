package xyz.brassgoggledcoders.transport.immersiveengineering;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.transport.immersiveengineering.compat.ImmersiveEngineeringHullTypes;
import xyz.brassgoggledcoders.transport.immersiveengineering.datagen.TransportIEEnglishLanguageProvider;
import xyz.brassgoggledcoders.transport.immersiveengineering.datagen.TransportIEItemModels;

@Mod(TransportImmersiveEngineering.ID)
public class TransportImmersiveEngineering {
    public static final String ID = "transport_ie";

    public TransportImmersiveEngineering() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ImmersiveEngineeringHullTypes.register(modEventBus);
        modEventBus.addListener(this::gatherData);
    }

    public void gatherData(GatherDataEvent event) {
        if (event.includeClient()) {
            event.getGenerator().addProvider(new TransportIEEnglishLanguageProvider(event.getGenerator()));
            event.getGenerator().addProvider(new TransportIEItemModels(event.getGenerator(), event.getExistingFileHelper()));
        }
    }
}
