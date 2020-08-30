package xyz.brassgoggledcoders.transport.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.brassgoggledcoders.transport.screen.note.NoteBookInfo;

import java.util.Objects;
import java.util.function.Supplier;

public class OpenNoteMessage {
    private final Hand hand;
    private final boolean edit;

    public OpenNoteMessage(Hand hand, boolean edit) {
        this.hand = hand;
        this.edit = edit;
    }

    public static OpenNoteMessage decode(PacketBuffer packetBuffer) {
        return new OpenNoteMessage(packetBuffer.readEnumValue(Hand.class), packetBuffer.readBoolean());
    }

    public static void encode(OpenNoteMessage openNoteMessage, PacketBuffer packetBuffer) {
        packetBuffer.writeEnumValue(openNoteMessage.hand);
        packetBuffer.writeBoolean(openNoteMessage.edit);
    }

    public static boolean consume(OpenNoteMessage openNoteMessage, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().setPacketHandled(true);
        contextSupplier.get().enqueueWork(() -> {
            if (openNoteMessage.edit) {
                LogicalSidedProvider.INSTANCE.<Minecraft>get(LogicalSide.CLIENT).displayGuiScreen(
                        new ReadBookScreen(new NoteBookInfo(
                                Objects.requireNonNull(LogicalSidedProvider.INSTANCE.<Minecraft>get(LogicalSide.CLIENT).player).getHeldItem(openNoteMessage.hand)
                        ))
                );
            }

        });
        return true;
    }
}
