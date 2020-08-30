package xyz.brassgoggledcoders.transport.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EditNoteMessage {
    private final Hand hand;
    private final String title;
    private final String text;

    public EditNoteMessage(Hand hand, String title, String text) {
        this.hand = hand;
        this.title = title;
        this.text = text;
    }

    public static EditNoteMessage decode(PacketBuffer packetBuffer) {
        return new EditNoteMessage(
                packetBuffer.readEnumValue(Hand.class),
                packetBuffer.readString(256),
                packetBuffer.readString()
        );
    }

    public static void encode(EditNoteMessage editNoteMessage, PacketBuffer packetBuffer) {
        packetBuffer.writeEnumValue(editNoteMessage.hand);
        packetBuffer.writeString(editNoteMessage.title, 256);
        packetBuffer.writeString(editNoteMessage.text, 32767);
    }

    public static boolean consume(EditNoteMessage editNoteMessage, Supplier<NetworkEvent.Context> contextSupplier) {
        return false;
    }
}
