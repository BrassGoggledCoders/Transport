package xyz.brassgoggledcoders.transport.item;

import com.mojang.datafixers.util.Function3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;

public class ShellMinecartItem<T extends AbstractMinecart> extends Item {
    private final Function3<ItemStack, Level, Vec3, T> minecartCreator;

    public ShellMinecartItem(Properties pProperties, Function3<ItemStack, Level, Vec3, T> minecartCreator) {
        super(pProperties);
        this.minecartCreator = minecartCreator;
    }

    @Override
    @Nonnull
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (!blockstate.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack itemStack = pContext.getItemInHand();
            if (!level.isClientSide) {
                RailShape railShape = blockstate.getBlock() instanceof BaseRailBlock ?
                        ((BaseRailBlock) blockstate.getBlock()).getRailDirection(blockstate, level, blockpos, null) :
                        RailShape.NORTH_SOUTH;
                double slopeOffset = 0.0D;
                if (railShape.isAscending()) {
                    slopeOffset = 0.5D;
                }

                Vec3 position = new Vec3(
                        (double) blockpos.getX() + 0.5D,
                        (double) blockpos.getY() + 0.0625D + slopeOffset,
                        (double) blockpos.getZ() + 0.5D
                );
                T minecart = minecartCreator.apply(
                        itemStack,
                        level,
                        position
                );

                if (itemStack.hasCustomHoverName()) {
                    minecart.setCustomName(itemStack.getHoverName());
                }

                level.addFreshEntity(minecart);
                level.gameEvent(pContext.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
            }

            itemStack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        TransportAPI.ITEM_HELPER.get()
                .appendTextForShellItems(pStack, pTooltipComponents::add);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        if (this.allowedIn(pCategory)) {
            Collection<ShellContentCreatorInfo> creatorInfos = TransportAPI.SHELL_CONTENT_CREATOR.get().getAll();
            if (creatorInfos.isEmpty()) {
                pItems.add(new ItemStack(this));
            } else {
                for (ShellContentCreatorInfo info : creatorInfos) {
                    ItemStack itemStack = new ItemStack(this);
                    info.embedNBT(itemStack);
                    pItems.add(itemStack);
                }
            }
        }
    }
}
