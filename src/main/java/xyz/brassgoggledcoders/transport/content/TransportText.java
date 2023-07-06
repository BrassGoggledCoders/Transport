package xyz.brassgoggledcoders.transport.content;

import net.minecraft.network.chat.Component;
import xyz.brassgoggledcoders.transport.Transport;

public class TransportText {

    public static final Component SHELL_CONTENT_COMPONENT = Transport.getRegistrate()
            .addLang("text", Transport.rl("shell_content"), "Shell Content: %s");

    public static void setup() {

    }
}
