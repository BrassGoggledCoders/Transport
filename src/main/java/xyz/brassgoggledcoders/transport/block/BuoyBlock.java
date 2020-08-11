package xyz.brassgoggledcoders.transport.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BuoyBlock extends Block {
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public BuoyBlock() {
        this(Properties.create(Material.IRON)
                .notSolid());
        this.setDefaultState(this.getStateContainer().getBaseState().with(TOP, false));
    }

    public BuoyBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TOP);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        if (context.getWorld().getBlockState(context.getPos().up()).isReplaceable(context)) {
            return this.getDefaultState();
        } else {
            return null;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        world.setBlockState(pos.up(), state.with(TOP, true), 3);
    }
}
