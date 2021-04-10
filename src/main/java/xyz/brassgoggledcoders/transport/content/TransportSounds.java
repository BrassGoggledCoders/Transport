package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.util.SoundEvent;
import xyz.brassgoggledcoders.transport.Transport;

public class TransportSounds {

    public static final RegistryEntry<SoundEvent> WHISTLE = Transport.getRegistrate()
            .object("whistle")
            .simple(SoundEvent.class, () -> new SoundEvent(Transport.rl("whistle")));

    public static void setup() {

    }
}
