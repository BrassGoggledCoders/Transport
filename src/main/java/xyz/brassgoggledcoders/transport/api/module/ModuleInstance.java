package xyz.brassgoggledcoders.transport.api.module;

import com.mojang.datafixers.util.Function3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
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
import java.util.UUID;

public class ModuleInstance<MOD extends Module<MOD>>
        implements INBTSerializable<CompoundNBT>, ICapabilityProvider {
    private final MOD module;
    private final IModularEntity modularEntity;

    private UUID uniqueId;

    protected ModuleInstance(MOD module, IModularEntity modularEntity) {
        this.module = module;
        this.modularEntity = modularEntity;
        this.uniqueId = UUID.randomUUID();
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
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putUniqueId("uniqueId", this.uniqueId);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.hasUniqueId("uniqueId")) {
            this.uniqueId = nbt.getUniqueId("uniqueId");
        }
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

    @Nonnull
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
        this.uniqueId = packetBuffer.readUniqueId();
    }

    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeUniqueId(this.uniqueId);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    @Nullable
    public Function3<Integer, PlayerInventory, PlayerEntity, ? extends Container> getContainerCreator() {
        return null;
    }

    @Nullable
    public ModuleTab createTab() {
        Function3<Integer, PlayerInventory, PlayerEntity, ? extends Container> container = this.getContainerCreator();
        if (container != null) {
            return new ModuleTab(
                    uniqueId, this.getDisplayName(),
                    this.asItemStack(),
                    container
            );
        } else {
            return null;
        }
    }
}
