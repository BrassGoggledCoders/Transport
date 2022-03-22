package xyz.brassgoggledcoders.transport.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.entity.ShellMinecart;

import javax.annotation.Nonnull;

public class ShellMinecartItem extends Item {
    public ShellMinecartItem(Properties pProperties) {
        super(pProperties);
        DispenserBlock.registerBehavior(this, new DispenserMinecartItemBehavior((itemStack, level, pos) -> {
            CompoundTag shellContentTag = itemStack.getTagElement("shellContent");
            ShellContent shellContent = TransportAPI.SHELL_CONTENT_CREATOR.get().create(shellContentTag);

            return new ShellMinecart(TransportEntities.SHELL_MINECART.get(), level, pos, shellContent);
        }));
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

                CompoundTag shellContentTag = itemStack.getTagElement("shellContent");
                ShellContent shellContent = TransportAPI.SHELL_CONTENT_CREATOR.get().create(shellContentTag);

                Vec3 position = new Vec3(
                        (double) blockpos.getX() + 0.5D,
                        (double) blockpos.getY() + 0.0625D + slopeOffset,
                        (double) blockpos.getZ() + 0.5D
                );
                AbstractMinecart minecart = new ShellMinecart(TransportEntities.SHELL_MINECART.get(), level, position,
                        shellContent);

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
}
