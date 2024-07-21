package xyz.brassgoggledcoders.transport.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import java.util.Optional;

public record NewGenerationClientMessage(
        int entityId,
        ShellContentCreatorInfo shellContentCreatorInfo
) implements CustomPacketPayload {
    public static final ResourceLocation ID = Transport.rl("new_generation");

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(entityId);
        Optional<CompoundTag> tag = shellContentCreatorInfo.asTag();

        friendlyByteBuf.writeBoolean(tag.isPresent());
        tag.ifPresent(friendlyByteBuf::writeNbt);
    }

    @Override
    @NotNull
    public ResourceLocation id() {
        return ID;
    }

    public static void consume(NewGenerationClientMessage clientMessage, IPayloadContext context) {
        context.workHandler()
                .execute(() -> context.level()
                        .ifPresent(level -> {
                            Entity entity = level.getEntity(clientMessage.entityId());
                            if (entity instanceof IShell shell) {
                                shell.getHolder()
                                        .update(clientMessage.shellContentCreatorInfo()
                                                .create(null)
                                        );
                            }
                        })
                );
    }

    public static NewGenerationClientMessage decode(FriendlyByteBuf friendlyByteBuf) {
        int entityId = friendlyByteBuf.readInt();
        if (friendlyByteBuf.readBoolean()) {
            return new NewGenerationClientMessage(
                    entityId,
                    ShellContentCreatorInfo.fromTag(friendlyByteBuf.readNbt())
            );
        } else {
            return new NewGenerationClientMessage(entityId, TransportAPI.SHELL_CONTENT_CREATOR.get().getEmpty());
        }
    }
}
