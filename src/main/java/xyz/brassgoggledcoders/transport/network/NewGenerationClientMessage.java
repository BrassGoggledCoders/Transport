package xyz.brassgoggledcoders.transport.network;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.service.ShellContentCreatorServiceImpl;

import java.util.Optional;
import java.util.function.Supplier;

public record NewGenerationClientMessage(
        int entityId,
        ShellContentCreatorInfo shellContentCreatorInfo
) {
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(entityId);
        Optional<Tag> tag = ShellContentCreatorInfo.CODEC
                .encode(shellContentCreatorInfo, NbtOps.INSTANCE, NbtOps.INSTANCE.empty())
                .result()
                .filter(CompoundTag.class::isInstance);

        friendlyByteBuf.writeBoolean(tag.isPresent());
        tag.map(CompoundTag.class::cast)
                .ifPresent(friendlyByteBuf::writeNbt);
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            LogicalSide logicalSide = contextSupplier.get().getDirection().getReceptionSide();
            Optional<Level> levelOpt = LogicalSidedProvider.CLIENTWORLD.get(logicalSide);
            if (levelOpt.isPresent()) {
                Level level = levelOpt.get();
                Entity entity = level.getEntity(this.entityId());
                if (entity instanceof IShell shell) {
                    shell.getHolder()
                            .update(this.shellContentCreatorInfo().create(null));
                }
            }
        });
        return true;
    }

    public static NewGenerationClientMessage decode(FriendlyByteBuf friendlyByteBuf) {
        int entityId = friendlyByteBuf.readInt();
        if (friendlyByteBuf.readBoolean()) {
            return new NewGenerationClientMessage(
                    entityId,
                    ShellContentCreatorInfo.CODEC.decode(NbtOps.INSTANCE, friendlyByteBuf.readNbt())
                            .resultOrPartial(error -> Transport.LOGGER.warn(entityId + " failed with error: " + error))
                            .map(Pair::getFirst)
                            .orElse(ShellContentCreatorServiceImpl.MISSING)
            );
        } else {
            return new NewGenerationClientMessage(entityId, ShellContentCreatorServiceImpl.MISSING);
        }
    }
}
