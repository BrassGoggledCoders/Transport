package xyz.brassgoggledcoders.transport.content;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.screen.util.FormattedLang;

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

    public static final ITextComponent SCREEN_FLUID_EMPTY = Transport.getRegistrate()
            .addRawLang("screen.transport.fluid.empty", "Empty");

    public static final ITextComponent SCREEN_FLUID_CAPACITY = Transport.getRegistrate()
            .addRawLang("screen.transport.fluid.capacity", "%s / %s mB");

    public static final ITextComponent TOOLTIP_CONTENTS_FLUID = Transport.getRegistrate()
            .addRawLang("tooltip.transport.contents.fluid", " * %s mB - %s");

    public static final ITextComponent TOOLTIP_CONTENTS_ITEM = Transport.getRegistrate()
            .addRawLang("tooltip.transport.contents.item", " * %sx - %s");

    public static final ITextComponent TOOLTIP_CONTENTS = Transport.getRegistrate()
            .addRawLang("tooltip.transport.contents", "Contents: ");

    public static final ITextComponent BLANK = Transport.getRegistrate()
            .addRawLang("screen.transport.blank", "Blank");

    public static final ITextComponent FORGE_ENERGY = Transport.getRegistrate()
            .addRawLang("text.transport.forge_energy", "FE")
            .mergeStyle(TextFormatting.DARK_AQUA);

    public static final ITextComponent MILLI_BUCKET = Transport.getRegistrate()
            .addRawLang("text.transport.milli_bucket", "mB")
            .mergeStyle(TextFormatting.DARK_AQUA);

    public static final FormattedLang FLUID = Transport.getRegistrate()
            .addFormattedLang("text.transport.fluid", "Fluid: %s")
            .withFormatting(TextFormatting.GOLD);

    public static final FormattedLang AMOUNT = Transport.getRegistrate()
            .addFormattedLang("text.transport.amount", "Amount: %1$s/%2$s%3$s")
            .withFormatting(TextFormatting.GOLD);

    public static void setup() {

    }
}
