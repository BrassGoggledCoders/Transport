package xyz.brassgoggledcoders.transport.block.jobsite;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.container.jobsite.RailWorkerBenchContainer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class RailWorkerBenchBlock extends SingleRecipeBlock {
    public RailWorkerBenchBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        return new SimpleNamedContainerProvider(new IContainerProvider() {
            @Override
            @ParametersAreNonnullByDefault
            public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new RailWorkerBenchContainer(windowId, playerInventory, IWorldPosCallable.of(worldIn, pos));
            }
        }, new TranslationTextComponent(this.getTranslationKey()));
    }
}
