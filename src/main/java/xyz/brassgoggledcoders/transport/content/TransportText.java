package xyz.brassgoggledcoders.transport.content;

import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.Transport;

@SuppressWarnings("unused")
public class TransportText {
    public static final ITextComponent HULL_TYPE_INFO = Transport.getRegistrate()
            .addRawLang("text.transport.hull_type", "Hull Type: %s");

    public static final ITextComponent MODULE_SLOT_INFO = Transport.getRegistrate()
            .addRawLang("text.transport.module_slot", "%s Module Slot");

    public static final ITextComponent INSTALLED_MODULES = Transport.getRegistrate()
            .addRawLang("text.transport.installed_modules", "Installed Modules");

    public static final ITextComponent INSTALLED_MODULE = Transport.getRegistrate()
            .addRawLang("text.transport.installed_module", " * %s - %s");

    public static final ITextComponent ITEM_GROUP = Transport.getRegistrate()
            .addRawLang("itemGroup.transport", "Transport");

    public static final ITextComponent GUIDE_NAME = Transport.getRegistrate()
            .addRawLang("guide.transport.name", "Advanced Transport");

    public static final ITextComponent GUIDE_LANDING_TEXT = Transport.getRegistrate()
            .addRawLang("guide.transport.landing_text", "Importing the Transportation of Goods");



    public static void setup() {

    }
}
