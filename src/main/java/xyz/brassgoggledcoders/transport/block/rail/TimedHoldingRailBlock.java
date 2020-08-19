package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.brassgoggledcoders.transport.tileentity.rail.TimedHoldingRailTileEntity;
import xyz.brassgoggledcoders.transport.util.RailUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class TimedHoldingRailBlock extends HoldingRailBlock {
    public static IntegerProperty DELAY = BlockStateProperties.DELAY_1_4;

    public TimedHoldingRailBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(DELAY, 1));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DELAY);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                                             BlockRayTraceResult rayTraceResult) {
        ActionResultType actionResultType = super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
        if (actionResultType == ActionResultType.PASS) {
            world.setBlockState(pos, state.cycle(DELAY));
            return ActionResultType.SUCCESS;
        } else {
            return actionResultType;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TimedHoldingRailTileEntity) {
            ((TimedHoldingRailTileEntity) tileEntity).onMinecartPass(cart);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TimedHoldingRailTileEntity();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @ParametersAreNonnullByDefault
    public void animateTick(BlockState blockState, World world, BlockPos pos, Random rand) {
        if (blockState.get(POWERED)) {
            if (rand.nextBoolean()) {
                Direction direction = RailUtils.getFacingFromBlockState(blockState).getOpposite();
                double d0 = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
                double d1 = (double) pos.getY() + 0.4D + (rand.nextDouble() - 0.5D) * 0.2D;
                double d2 = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
                float f = (float) (blockState.get(DELAY) * 2 - 1);

                f = f / 16.0F;
                double d3 = f * (float) direction.getXOffset();
                double d4 = f * (float) direction.getZOffset();
                world.addParticle(RedstoneParticleData.REDSTONE_DUST, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
