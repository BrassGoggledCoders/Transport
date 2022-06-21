package xyz.brassgoggledcoders.transport.api.shellcontent;

import com.mojang.serialization.Codec;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ShellContentType<T extends IShellContentCreator<?>> extends ForgeRegistryEntry<ShellContentType<?>> {
    private final Codec<T> codec;

    public ShellContentType(Codec<T> codec) {
        this.codec = codec;
    }

    public Codec<T> getCodec() {
        return this.codec;
    }
}
