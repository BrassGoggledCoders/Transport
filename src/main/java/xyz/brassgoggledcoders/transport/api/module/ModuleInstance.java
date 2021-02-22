package xyz.brassgoggledcoders.transport.api.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleTab;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModuleInstance<MOD extends Module<MOD>>
        implements INBTSerializable<CompoundNBT>, ICapabilityProvider {
    private final MOD module;
    private final IModularEntity modularEntity;

    protected ModuleInstance(MOD module, IModularEntity modularEntity) {
        this.module = module;
        this.modularEntity = modularEntity;
    }

    public void tick() {

    }

    public ActionResultType applyInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        return ActionResultType.PASS;
    }

    public int getComparatorLevel() {
        return -1;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    public void onActivatorPass(boolean receivingPower) {

    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
    }

    public MOD getModule() {
        return module;
    }

    public World getWorld() {
        return this.getModularEntity().getTheWorld();
    }

    public ModuleType getModuleType() {
        return this.getModule().getType();
    }

    public IModularEntity getModularEntity() {
        return modularEntity;
    }

    public ITextComponent getDisplayName() {
        return this.getModule().getDisplayName();
    }

    public ItemStack asItemStack() {
        return new ItemStack(this.getModule().asItem());
    }

    public void receiveClientUpdate(int type, @Nullable CompoundNBT compoundNBT) {

    }

    public void sendClientUpdate(int type, @Nullable CompoundNBT compoundNBT) {
        this.getModularEntity().sendClientUpdate(this, type, compoundNBT);
    }

    public void invalidateCapabilities() {
    }

    public void read(PacketBuffer packetBuffer) {

    }

    public void write(PacketBuffer packetBuffer) {

    }

    @Nullable
    public ModuleTab<?> createTab() {
        return null;
    }
}
