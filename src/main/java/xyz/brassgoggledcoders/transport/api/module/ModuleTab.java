package xyz.brassgoggledcoders.transport.api.module;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

import java.util.UUID;

public class ModuleTab {
    private final UUID uniqueId;
    private final ITextComponent displayName;
    private final ItemStack displayStack;

    public ModuleTab(
            UUID uniqueId,
            ITextComponent displayName,
            ItemStack displayStack
    ) {
        this.uniqueId = uniqueId;
        this.displayName = displayName;
        this.displayStack = displayStack;
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

    public void toPacketBuffer(PacketBuffer packetBuffer) {
        packetBuffer.writeUniqueId(uniqueId);
        packetBuffer.writeTextComponent(displayName);
        packetBuffer.writeItemStack(displayStack);
    }

    public static ModuleTab fromPacketBuffer(PacketBuffer packetBuffer) {
        return new ModuleTab(
                packetBuffer.readUniqueId(),
                packetBuffer.readTextComponent(),
                packetBuffer.readItemStack()
        );
    }

}
