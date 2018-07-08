package xyz.brassgoggledcoders.transport.rails.block;

import com.teamacronymcoders.base.Capabilities;
import com.teamacronymcoders.base.blocks.IHasBlockStateMapper;
import com.teamacronymcoders.base.util.CapUtils;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.library.block.rail.BlockRailFoundation;
import xyz.brassgoggledcoders.transport.library.block.rail.RailPredicates;

public class BlockRailHolding extends BlockRailFoundation implements IHasBlockStateMapper {
    public static final PropertyBool NORTH_WEST = PropertyBool.create("north_west");
    public static final PropertyEnum<EnumRailDirection> SHAPE = PropertyEnum.create("shape", EnumRailDirection.class,
            RailPredicates.FLAT_STRAIGHT::test);
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockRailHolding() {
        super("holding");
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(POWERED, false)
                .withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH).withProperty(NORTH_WEST, true));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && CapUtils.getOptional(player.getHeldItem(hand), Capabilities.TOOL).isPresent()) {
            state = state.withProperty(NORTH_WEST, !state.getValue(NORTH_WEST));
            world.setBlockState(pos, state, 3);
            return true;
        }
        return false;
    }

    @Override
    public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getValue(POWERED)) {
            float speedIncrease = .5f;
            if (state.getValue(NORTH_WEST)) {
                speedIncrease *= -1;
            }
            if (state.getValue(SHAPE) == EnumRailDirection.NORTH_SOUTH) {
                cart.motionZ += speedIncrease;
            } else {
                cart.motionX += speedIncrease;
            }
        } else {
            cart.motionX = 0;
            cart.motionY = 0;
            cart.motionZ = 0;
        }
    }

    @Override
    protected void updateState(IBlockState state, World world, BlockPos pos, Block block) {
        boolean isStatePowered = state.getValue(POWERED);
        boolean isWorldPowered = world.isBlockPowered(pos);

        if (isWorldPowered != isStatePowered) {
            world.setBlockState(pos, state.withProperty(POWERED, isWorldPowered), 3);
            world.notifyNeighborsOfStateChange(pos.down(), this, true);
        }
        super.updateState(state, world, pos, block);
    }

    @Override
    public boolean canMakeSlopes(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SHAPE, POWERED, NORTH_WEST);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        IBlockState blockState = this.getDefaultState();
        if (meta > 7) {
            meta = meta - 8;
            blockState = blockState.withProperty(POWERED, true);
        }
        if (meta % 2 != 0) {
            blockState = blockState.withProperty(NORTH_WEST, false);
        }
        if (meta > 1) {
            blockState = blockState.withProperty(SHAPE, EnumRailDirection.EAST_WEST);
        }

        return blockState;
    }

    @Override
    public int getMetaFromState(IBlockState blockState) {
        int facing = blockState.getValue(SHAPE) == EnumRailDirection.NORTH_SOUTH ? 0 : 2;
        facing += blockState.getValue(NORTH_WEST) ? 0 : 1;
        int powered = blockState.getValue(POWERED) ? 8 : 0;
        return facing + powered;
    }

    @Override
    public String getVariant(IBlockState blockState) {
        int facing = blockState.getValue(SHAPE) == EnumRailDirection.NORTH_SOUTH ? 0 : 2;
        facing += blockState.getValue(NORTH_WEST) ? 0 : 1;
        return "facing=" + EnumFacing.VALUES[facing + 2] + ",powered=" + blockState.getValue(POWERED).toString();
    }

    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return SHAPE;
    }
}
