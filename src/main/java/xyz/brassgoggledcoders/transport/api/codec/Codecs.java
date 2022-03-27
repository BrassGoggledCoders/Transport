package xyz.brassgoggledcoders.transport.api.codec;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public class Codecs {
    public static final Codec<Component> COMPONENT = new ComponentCodec();
}
