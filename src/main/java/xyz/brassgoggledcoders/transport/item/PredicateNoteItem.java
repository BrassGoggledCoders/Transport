package xyz.brassgoggledcoders.transport.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import xyz.brassgoggledcoders.transport.api.podium.BookHolder;
import xyz.brassgoggledcoders.transport.api.podium.IBookHolder;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateRegistry;

import javax.annotation.Nonnull;

public class PredicateNoteItem extends Item {
    public PredicateNoteItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
        TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
        IBookHolder bookHolder = null;
        if (tileEntity instanceof LecternTileEntity) {
            LecternTileEntity lecternTileEntity = (LecternTileEntity) tileEntity;
            bookHolder = new BookHolder(lecternTileEntity.getBook(), lecternTileEntity.getPage());
        } else if (tileEntity instanceof IBookHolder) {
            bookHolder = (IBookHolder) tileEntity;
        }
        if (bookHolder != null) {
            String predicateInput = PredicateRegistry.getPredicateInputFromItemStack(bookHolder.getBook(), bookHolder);
            if (predicateInput != null) {
                context.getItem().getOrCreateChildTag("predicate").putString("input", predicateInput);
                return ActionResultType.func_233537_a_(context.getWorld().isRemote);
            }
        }
        return super.onItemUse(context);
    }
}
