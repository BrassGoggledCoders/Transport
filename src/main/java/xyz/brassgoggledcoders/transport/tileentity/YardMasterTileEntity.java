package xyz.brassgoggledcoders.transport.tileentity;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.block.YardMasterBlock;

import javax.annotation.Nonnull;

public class YardMasterTileEntity extends TileEntity implements ITickableTileEntity {
    private AxisAlignedBB yardBoundaries;
    private boolean placed = false;
    private BlockPos farCorner;
    private BlockPos nearCorner;

    public YardMasterTileEntity(TileEntityType<? extends YardMasterTileEntity> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void tick() {
        if (yardBoundaries == null && this.getWorld() != null) {
            Direction facing = this.getBlockState().get(YardMasterBlock.FACING);
            BlockPos blockPos = this.getPos();
            farCorner = blockPos.offset(facing, 8).offset(facing.rotateY(), 4).up(6);
            nearCorner = blockPos.offset(facing.rotateYCCW(), 4).down(2);
            this.yardBoundaries = new AxisAlignedBB(farCorner.getX(), farCorner.getY(), farCorner.getZ(),
                    nearCorner.getX(), nearCorner.getY(), nearCorner.getZ());
        }
        if (yardBoundaries != null && this.getWorld() != null && !placed) {
            this.getWorld().setBlockState(farCorner, Blocks.ACACIA_FENCE.getDefaultState());
            this.getWorld().setBlockState(nearCorner, Blocks.OAK_FENCE.getDefaultState());
            placed = true;
        }
    }

    @Override
    public void setPos(@Nonnull BlockPos blockPos) {
        super.setPos(blockPos);
        this.handleNewPos();
    }

    @Override
    public void setWorldAndPos(@Nonnull World world, @Nonnull BlockPos pos) {
        super.setWorldAndPos(world, pos);
        this.handleNewPos();
    }

    private void handleNewPos() {
        this.yardBoundaries = null;
    }
}
