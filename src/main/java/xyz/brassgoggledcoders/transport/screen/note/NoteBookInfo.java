package xyz.brassgoggledcoders.transport.screen.note;

import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextProperties;

import javax.annotation.Nonnull;
import java.util.List;

public class NoteBookInfo implements ReadBookScreen.IBookInfo {
    private final String text;

    public NoteBookInfo(ItemStack itemStack) {
        if (itemStack.getTag() != null) {
            List<String> pages = ReadBookScreen.nbtPagesToStrings(itemStack.getTag());
            if (pages.isEmpty()) {
                text = "";
            } else {
                text = pages.get(0);
            }
        } else {
            text = "";
        }
    }

    @Override
    public int getPageCount() {
        return 1;
    }

    @Override
    @Nonnull
    public ITextProperties func_230456_a_(int p_230456_1_) {
        return ITextProperties.func_240652_a_(text);
    }
}
