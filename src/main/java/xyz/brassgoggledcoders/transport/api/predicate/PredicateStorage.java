package xyz.brassgoggledcoders.transport.api.predicate;

import com.mojang.datafixers.util.Either;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WritableBookItem;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class PredicateStorage {
    private Either<String, Predicate<Entity>> predicate = null;

    @Nonnull
    public Either<String, Predicate<Entity>> getPredicate(TileEntity tileEntity) {
        if (predicate == null) {
            if (tileEntity instanceof LecternTileEntity) {
                LecternTileEntity lecternTileEntity = (LecternTileEntity) tileEntity;
                if (lecternTileEntity.hasBook()) {
                    ItemStack book = lecternTileEntity.getBook();
                    String routingString = null;
                    if (WrittenBookItem.validBookTagContents(book.getTag())) {
                        CompoundNBT bookNBT = book.getTag();
                        ListNBT pagesNBT = bookNBT.getList("pages", Constants.NBT.TAG_STRING);
                        String currentPage = pagesNBT.getString(lecternTileEntity.getPage());
                        ITextComponent textComponent = TextComponent.Serializer.func_240643_a_(currentPage);
                        if (textComponent != null) {
                            routingString = textComponent.getString();
                        }
                    } else if (WritableBookItem.isNBTValid((book.getTag()))) {
                        CompoundNBT bookNBT = book.getTag();
                        ListNBT pagesNBT = bookNBT.getList("pages", Constants.NBT.TAG_STRING);
                        routingString = pagesNBT.getString(lecternTileEntity.getPage());
                    }

                    if (routingString != null) {
                        try {
                            this.predicate = Either.right(PredicateParser.fromString(routingString).getNextEntityPredicate());
                        } catch (PredicateParserException exception) {
                            this.predicate = Either.left(exception.getMessage());
                        }
                    } else {
                        this.predicate = Either.left("Found Invalid Book");
                    }
                } else {
                    this.predicate = Either.left("No Book found");
                }
            } else {
                this.predicate = Either.left("Didn't find Lectern");
            }
        }

        return predicate;
    }

    public void invalidate() {
        predicate = null;
    }
}
