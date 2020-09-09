package xyz.brassgoggledcoders.transport.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.tileentity.YardMasterObject;
import xyz.brassgoggledcoders.transport.tileentity.YardMasterTileEntity;

import javax.annotation.Nonnull;

public class RailBreakerItem extends Item {
    public RailBreakerItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
        return state.isIn(BlockTags.RAILS) ? 15F : 1F;
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
        World world = context.getWorld();
        CompoundNBT yardMasterNBT = context.getItem().getChildTag("yardMaster");
        if (yardMasterNBT != null) {
            BlockPos yardMasterPos = BlockPos.fromLong(yardMasterNBT.getLong("pos"));
            if (context.getPos().distanceSq(yardMasterPos) < 20F) {
                TileEntity tileEntity = world.getTileEntity(yardMasterPos);
                if (tileEntity instanceof YardMasterTileEntity) {
                    if (((YardMasterTileEntity) tileEntity).addConnectedObject(new YardMasterObject(context.getPos(),
                            new ItemStack(world.getBlockState(context.getPos()).getBlock())))) {
                        context.getItem().removeChildTag("yardMaster");
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            context.getItem().removeChildTag("yardMaster");
        } else {
            TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
            if (tileEntity instanceof YardMasterTileEntity) {
                CompoundNBT compoundNBT = context.getItem().getOrCreateChildTag("yardMaster");
                compoundNBT.putLong("pos", context.getPos().toLong());
                return ActionResultType.SUCCESS;
            }
        }

        return super.onItemUse(context);
    }
}
