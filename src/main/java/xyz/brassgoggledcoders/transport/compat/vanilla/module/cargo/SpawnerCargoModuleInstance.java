package xyz.brassgoggledcoders.transport.compat.vanilla.module.cargo;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpawnerCargoModuleInstance extends CargoModuleInstance {
    private final AbstractSpawner spawner;

    public SpawnerCargoModuleInstance(CargoModule cargoModule, IModularEntity modularEntity) {
        super(cargoModule, modularEntity);
        this.spawner = new CargoSpawner(modularEntity, this);
    }

    @Override
    public void tick() {
        super.tick();
        this.spawner.tick();
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        super.write(packetBuffer);
        CompoundNBT compoundNBT = this.spawner.write(new CompoundNBT());
        compoundNBT.remove("SpawnPotentials");
        packetBuffer.writeCompoundTag(compoundNBT);
    }

    @Override
    public void read(PacketBuffer packetBuffer) {
        super.read(packetBuffer);
        CompoundNBT compoundNBT = packetBuffer.readCompoundTag();
        if (compoundNBT != null) {
            this.spawner.read(compoundNBT);
        }
    }

    @Override
    public void receiveClientUpdate(int type, @Nullable CompoundNBT compoundNBT) {
        super.receiveClientUpdate(type, compoundNBT);
        if (compoundNBT != null) {
            this.spawner.read(compoundNBT);
        } else {
            this.spawner.setDelayToMin(type);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = super.serializeNBT();
        this.spawner.write(compoundNBT);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.spawner.read(nbt);
    }

    @Override
    public void readFromItemStack(ItemStack itemStack) {
        super.readFromItemStack(itemStack);
        CompoundNBT blockEntityTag = itemStack.getChildTag("BlockEntityTag");
        if (blockEntityTag != null) {
            this.spawner.read(blockEntityTag);
        }
    }

    @Nonnull
    @Override
    public ItemStack asItemStack() {
        ItemStack itemStack = super.asItemStack();
        this.spawner.write(itemStack.getOrCreateChildTag("BlockEntityTag"));
        return itemStack;
    }

    public AbstractSpawner getSpawner() {
        return this.spawner;
    }

    private static final class CargoSpawner extends AbstractSpawner {
        private final IModularEntity entity;
        private final ModuleInstance<?> moduleInstance;

        private CargoSpawner(IModularEntity entity, ModuleInstance<?> moduleInstance) {
            this.entity = entity;
            this.moduleInstance = moduleInstance;
        }

        @Override
        public void broadcastEvent(int id) {
            entity.sendClientUpdate(moduleInstance, id, null);
        }

        @Override
        @Nonnull
        public World getWorld() {
            return entity.getTheWorld();
        }

        @Override
        @Nonnull
        public BlockPos getSpawnerPosition() {
            return entity.getSelf().getPosition();
        }

        @Override
        public Entity getSpawnerEntity() {
            return entity.getSelf();
        }

        @Override
        public void setNextSpawnData(@Nonnull WeightedSpawnerEntity nextSpawnData) {
            super.setNextSpawnData(nextSpawnData);
            CompoundNBT compoundNBT = this.write(new CompoundNBT());
            compoundNBT.remove("SpawnPotentials");
            entity.sendClientUpdate(moduleInstance, -1, compoundNBT);
        }
    }
}
