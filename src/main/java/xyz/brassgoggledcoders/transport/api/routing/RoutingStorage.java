package xyz.brassgoggledcoders.transport.api.routing;

import com.mojang.datafixers.util.Either;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;

public class RoutingStorage {
    private Either<String, Routing> routing = null;

    @Nonnull
    public Either<String, Routing> getRouting(TileEntity tileEntity) {
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
                        } else {
                            this.routing = Either.left("Failed to find Input for Routing");
                        }
                    } else {
                        this.routing = Either.left("Found Invalid Book");
                    }
                } else {
                    this.routing = Either.left("No Book found");
                }
            } else {
                this.routing = Either.left("Didn't find Lectern");
            }
        }

        return routing;
    }

    public void invalidate() {
        routing = null;
    }
}
