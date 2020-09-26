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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.master.IManageable;
import xyz.brassgoggledcoders.transport.api.master.ManagedObject;
import xyz.brassgoggledcoders.transport.content.TransportText;

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
        CompoundNBT managerNBT = context.getItem().getChildTag("manager");
        if (managerNBT != null) {
            BlockPos managerPos = BlockPos.fromLong(managerNBT.getLong("pos"));
            if (context.getPos().distanceSq(managerPos) < 20F) {
                TileEntity managerTileEntity = world.getTileEntity(managerPos);
                TileEntity manageableTileEntity = world.getTileEntity(context.getPos());
                if (managerTileEntity != null && manageableTileEntity != null) {
                    boolean connected = managerTileEntity.getCapability(TransportAPI.MANAGER)
                            .map(manager -> {
                                LazyOptional<IManageable> manageable = manageableTileEntity.getCapability(
                                        TransportAPI.MANAGEABLE);
                                if (manageable.isPresent()) {
                                    return manageable.map(manager::addManageable).orElse(false);
                                } else {
                                    return manager.addManagedObject(new ManagedObject(context.getPos(),
                                            new ItemStack(world.getBlockState(context.getPos()).getBlock())));
                                }
                            })
                            .orElse(false);
                    if (connected) {
                        context.getItem().removeChildTag("manager");
                        TransportText.MANAGER_LINKING_SUCCESS.send(context.getPlayer(), true);
                        return ActionResultType.SUCCESS;
                    } else {
                        TransportText.MANAGER_LINKING_FAIL.send(context.getPlayer(), true);
                    }
                }
            }
            context.getItem().removeChildTag("manager");
        } else {
            TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
            if (tileEntity != null && tileEntity.getCapability(TransportAPI.MANAGER).isPresent()) {
                CompoundNBT compoundNBT = context.getItem().getOrCreateChildTag("manager");
                compoundNBT.putLong("pos", context.getPos().toLong());
                TransportText.MANAGER_LINKING_START.send(context.getPlayer(), true, new TranslationTextComponent(
                        tileEntity.getBlockState()
                                .getBlock()
                                .getTranslationKey()
                ));
                return ActionResultType.SUCCESS;
            }
        }

        return super.onItemUse(context);
    }
}
