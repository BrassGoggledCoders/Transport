package xyz.brassgoggledcoders.transport.api.module.container;

import com.mojang.datafixers.util.Function3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.UUID;

public class ModuleTab {
    private final UUID uniqueId;
    private final ITextComponent displayName;
    private final ItemStack displayStack;
    private final Function3<Integer, PlayerInventory, PlayerEntity, ? extends Container> containerCreator;

    public ModuleTab(
            UUID uniqueId,
            ITextComponent displayName,
            ItemStack displayStack,
            Function3<Integer, PlayerInventory, PlayerEntity, ? extends Container> containerCreator
    ) {
        this.uniqueId = uniqueId;
        this.displayName = displayName;
        this.displayStack = displayStack;
        this.containerCreator = containerCreator;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public ITextComponent getDisplayName() {
        return displayName;
    }

    public ItemStack getDisplayStack() {
        return displayStack;
    }

    @Nullable
    public Container create(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        if (containerCreator != null) {
            return containerCreator.apply(id, playerInventory, playerEntity);
        } else {
            return null;
        }
    }

    public void toPacketBuffer(PacketBuffer packetBuffer) {
        packetBuffer.writeUniqueId(uniqueId);
        packetBuffer.writeTextComponent(displayName);
        packetBuffer.writeItemStack(displayStack);
    }

    public static ModuleTab fromPacketBuffer(PacketBuffer packetBuffer) {
        return new ModuleTab(
                packetBuffer.readUniqueId(),
                packetBuffer.readTextComponent(),
                packetBuffer.readItemStack(),
                null
        );
    }

}
