package xyz.brassgoggledcoders.transport.api.podium;

import net.minecraft.item.ItemStack;

public class BookHolder implements IBookHolder {
    private final ItemStack bookStack;
    private final int page;

    public BookHolder(ItemStack bookStack, int page) {
        this.bookStack = bookStack;
        this.page = page;
    }

    @Override
    public ItemStack getBook() {
        return bookStack;
    }

    @Override
    public int getOpenPage() {
        return page;
    }
}
