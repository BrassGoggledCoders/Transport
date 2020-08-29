package xyz.brassgoggledcoders.transport.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.podium.IPodium;
import xyz.brassgoggledcoders.transport.api.podium.PodiumBehavior;
import xyz.brassgoggledcoders.transport.api.podium.PodiumInventory;
import xyz.brassgoggledcoders.transport.block.PodiumBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

public class PodiumTileEntity extends TileEntity implements IPodium {
    private final PodiumInventory podiumInventory;
    private PodiumBehavior podiumBehavior = null;

    public PodiumTileEntity(TileEntityType<? extends PodiumTileEntity> tileEntityType) {
        super(tileEntityType);
        this.podiumInventory = new PodiumInventory(ItemStack.EMPTY, this::canPlayerUse);
        this.podiumInventory.addListener(inventory -> this.setHasItem(!inventory.getStackInSlot(0).isEmpty()));
    }

    public ActionResultType activateBehavior(PlayerEntity playerEntity) {
        if (this.getPodiumBehavior() != null) {
            return this.getPodiumBehavior().activate(playerEntity);
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState blockState, CompoundNBT nbt) {
        super.read(blockState, nbt);
        this.setDisplayItemStack(ItemStack.read(nbt.getCompound("displayItemStack")));
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("displayItemStack", this.getDisplayItemStack().write(new CompoundNBT()));
        return nbt;
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.put("displayItemStack", this.getDisplayItemStack().write(new CompoundNBT()));
        return nbt;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.setDisplayItemStack(ItemStack.read(tag.getCompound("displayItemStack")));
    }

    public ItemStack takeDisplayItemStack() {
        ItemStack itemStack = this.getDisplayItemStack();
        this.setDisplayItemStack(ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setDisplayItemStack(@Nonnull ItemStack itemStack) {
        this.podiumInventory.setInventorySlotContents(0, itemStack);
        if (!itemStack.isEmpty()) {
            this.podiumBehavior = TransportAPI.getPodiumBehavior(itemStack.getItem(), this);
            this.setHasItem(true);
        } else {
            this.podiumBehavior = null;
            this.setHasItem(false);
        }
    }

    public void setHasItem(boolean hasItem) {
        if (this.getWorld() != null) {
            this.getWorld().setBlockState(this.getPos(), this.getBlockState().with(PodiumBlock.HAS_ITEM, hasItem),
                    Constants.BlockFlags.DEFAULT);
        }
    }

    @Override
    public void pulseRedstone(int power) {

    }

    @Nonnull
    @Override
    public PodiumInventory getPodiumInventory() {
        return this.podiumInventory;
    }

    @Nonnull
    public ItemStack getDisplayItemStack() {
        return this.podiumInventory.getStackInSlot(0);
    }


    @Nonnull
    @Override
    public BlockPos getPodiumPos() {
        return this.getPos();
    }

    @Nonnull
    @Override
    public BlockState getPodiumBlockState() {
        return this.getBlockState();
    }


    @Nonnull
    @Override
    public IWorld getPodiumWorld() {
        return Objects.requireNonNull(this.getWorld());
    }

    @Nullable
    public PodiumBehavior getPodiumBehavior() {
        return podiumBehavior;
    }

    public boolean renderBook() {
        return this.getPodiumBehavior() != null && this.getPodiumBehavior().isBook();
    }

    public boolean canPlayerUse(PlayerEntity playerEntity) {
        return true;
    }
}
