package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ResourceLocationDataSerializer implements IDataSerializer<ResourceLocation> {
    @Override
    @ParametersAreNonnullByDefault
    public void write(PacketBuffer packetBuffer, ResourceLocation resourceLocation) {
        packetBuffer.writeResourceLocation(resourceLocation);
    }

    @Override
    @Nonnull
    public ResourceLocation read(PacketBuffer packetBuffer) {
        return packetBuffer.readResourceLocation();
    }

    @Override
    @Nonnull
    public ResourceLocation copyValue(ResourceLocation resourceLocation) {
        return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath());
    }
}
