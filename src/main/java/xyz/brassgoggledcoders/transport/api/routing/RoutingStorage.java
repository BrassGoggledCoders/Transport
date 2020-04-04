package xyz.brassgoggledcoders.transport.api.routing;

import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.transport.api.routing.instruction.FalseRouting;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

public class RoutingStorage {
    private Routing routing = null;

    public Routing getRouting(TileEntity tileEntity) {
        if (routing == null) {
            if (tileEntity instanceof LecternTileEntity) {
                LecternTileEntity lecternTileEntity = (LecternTileEntity) tileEntity;
                if (lecternTileEntity.hasBook()) {
                    ItemStack book = lecternTileEntity.getBook();
                    if (WrittenBookItem.validBookTagContents(book.getTag())) {
                        CompoundNBT bookNBT = book.getTag();
                        ListNBT pagesNBT = bookNBT.getList("pages", Constants.NBT.TAG_STRING);
                        String currentPage = pagesNBT.getString(lecternTileEntity.getPage());
                        ITextComponent textComponent = TextComponent.Serializer.fromJson(currentPage);
                        if (textComponent != null) {
                            this.routing = RoutingParser.parse(textComponent.getString());
                        }
                    }
                }
            }
        }

        if (routing == null) {
            routing = new FalseRouting();
        }

        return routing;
    }

    public void invalidate() {
        routing = null;
    }
}
