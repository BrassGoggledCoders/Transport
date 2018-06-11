package xyz.brassgoggledcoders.transport.library.tileentity.loader;

import com.teamacronymcoders.base.blocks.properties.SideType;
import com.teamacronymcoders.base.tileentities.TileEntitySidedBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;
import java.util.Optional;

public abstract class TileEntityLoaderBase<CAP> extends TileEntitySidedBase<CAP> implements ITickable {
    int updateTest = -1;

    @Override
    public void update() {
        if (!this.getWorld().isRemote && this.getWorld().getWorldTime() % 10 == updateTest) {
            int x = this.getPos().getX();
            int y = this.getPos().getY();
            int z = this.getPos().getZ();
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - 1, y - 1, z - 1, x + 2, y + 2, z + 2);
            List<Entity> entities = this.world.getEntitiesInAABBexcluding(null, axisAlignedBB, Entity::isEntityAlive);
            for (EnumFacing facing : EnumFacing.VALUES) {
                SideType sideType = this.getSideValue(facing);
                if (sideType != SideType.NONE) {
                    IBlockState otherBlockState = world.getBlockState(pos.offset(facing));
                    if (otherBlockState.isFullBlock()) {
                        //noinspection ResultOfMethodCallIgnored
                        tryTransferToTile(sideType, facing);
                    } else {
                        if (otherBlockState.getBlock() == Blocks.AIR || !tryTransferToTile(sideType, facing)) {
                            for (Entity entity : entities) {
                                if (transferToEntity(sideType, facing, entity)) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateTest = this.getWorld().rand.nextInt(10);
    }

    private boolean transferToEntity(SideType sideType, EnumFacing facing, Entity entity) {
        EnumFacing opposite = facing.getOpposite();
        if (entity.getPosition().equals(this.getPos().offset(facing))) {
            if (entity.hasCapability(this.getCapabilityType(), opposite)) {
                return transfer(sideType, entity.getCapability(this.getCapabilityType(), opposite));
            }
        }
        return false;
    }

    private boolean transfer(SideType sideType, CAP otherCapability) {
        if (sideType == SideType.INPUT) {
            return transfer(otherCapability, this.getInternalCapability());
        } else if (sideType == SideType.OUTPUT) {
            return transfer(this.getInternalCapability(), otherCapability);
        } else {
            return false;
        }
    }

    protected abstract boolean transfer(CAP from, CAP to);

    private boolean tryTransferToTile(SideType sideType, EnumFacing facing) {
        return Optional.ofNullable(world.getTileEntity(this.getPos().offset(facing)))
                .filter(tileEntity -> tileEntity.hasCapability(this.getCapabilityType(), facing.getOpposite()))
                .map(tileEntity -> tileEntity.getCapability(this.getCapabilityType(), facing.getOpposite()))
                .map(cap -> transfer(sideType, cap))
                .orElse(false);
    }
}
