package xyz.brassgoggledcoders.transport.library.tileentity.loader;

import com.teamacronymcoders.base.blocks.properties.SideType;
import com.teamacronymcoders.base.tileentities.TileEntitySidedBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public abstract class TileEntityLoaderBase<CAP> extends TileEntitySidedBase<CAP> implements ITickable {
    @Override
    public void update() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            SideType sideType = this.getSideValue(facing);
            if (sideType != SideType.NONE) {
                IBlockState otherBlockState = world.getBlockState(pos.offset(facing));
                if (otherBlockState.isFullBlock()) {
                    //noinspection ResultOfMethodCallIgnored
                    tryTransferToTile(sideType, facing);
                } else {
                    if (otherBlockState.getBlock() == Blocks.AIR || !tryTransferToTile(sideType, facing)) {
                        transferToEntity(sideType, facing);
                    }
                }
            }
        }
    }

    private void transferToEntity(SideType sideType, EnumFacing facing) {
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));

        for (Entity entity : entities) {
            if (entity.hasCapability(this.getCapabilityType(), facing.getOpposite())) {
                if (transfer(sideType, entity.getCapability(this.getCapabilityType(), facing))) {
                    break;
                }
            }
        }
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
        return false;
    }
}
