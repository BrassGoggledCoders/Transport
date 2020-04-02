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
import xyz.brassgoggledcoders.transport.api.routing.instruction.FalseInstruction;
import xyz.brassgoggledcoders.transport.api.routing.instruction.RoutingInstruction;

public class RoutingStorage {
    private RoutingInstruction routingInstruction = null;

    public RoutingInstruction getRouting(TileEntity tileEntity) {
        if (routingInstruction == null) {
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
                            this.routingInstruction = RoutingParser.parse(textComponent.getString());
                        }
                    }
                }
            }
        }

        if (routingInstruction == null) {
            routingInstruction = new FalseInstruction();
        }

        return routingInstruction;
    }

    public void invalidate() {
        routingInstruction = null;
    }
}
