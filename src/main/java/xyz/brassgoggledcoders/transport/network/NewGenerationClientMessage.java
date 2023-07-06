package xyz.brassgoggledcoders.transport.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import java.util.Optional;
import java.util.function.Supplier;

public record NewGenerationClientMessage(
        int entityId,
        ShellContentCreatorInfo shellContentCreatorInfo
) {
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(entityId);
        Optional<CompoundTag> tag = shellContentCreatorInfo.asTag();

        friendlyByteBuf.writeBoolean(tag.isPresent());
        tag.ifPresent(friendlyByteBuf::writeNbt);
    }

    public void consume(Supplier<NetworkEvent.Context> contextSupplier) {
        LogicalSide logicalSide = contextSupplier.get().getDirection().getReceptionSide();
        LogicalSidedProvider.CLIENTWORLD.get(logicalSide)
                .ifPresent(level -> {
                    Entity entity = level.getEntity(this.entityId());
                    if (entity instanceof IShell shell) {
                        shell.getHolder()
                                .update(this.shellContentCreatorInfo().create(null));
                    }
                });
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
