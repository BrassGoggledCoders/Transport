package xyz.brassgoggledcoders.transport.cargoinstance;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;

import static net.minecraft.block.CakeBlock.BITES;

public class CakeCargoModuleInstance extends CargoModuleInstance {
    private BlockState cakeBlockState;

    public CakeCargoModuleInstance(CargoModule cargoModule, IModularEntity modularEntity) {
        super(cargoModule, modularEntity);
        this.cakeBlockState = Blocks.CAKE.getDefaultState();
    }

    @Override
    public BlockState getBlockState() {
        return cakeBlockState;
    }

    @Override
    public int getComparatorLevel() {
        return (7 - this.getBlockState().get(BITES)) * 2;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.put("cake", NBTUtil.writeBlockState(cakeBlockState));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.cakeBlockState = NBTUtil.readBlockState(nbt.getCompound("cake"));
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        super.write(packetBuffer);
        packetBuffer.writeInt(Block.getStateId(this.cakeBlockState));
    }

    @Override
    public void read(PacketBuffer packetBuffer) {
        super.read(packetBuffer);
        this.cakeBlockState = Block.getStateById(packetBuffer.readInt());
    }

    @Override
    public ActionResultType applyInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        if (!player.canEat(false)) {
            return ActionResultType.PASS;
        } else {
            player.addStat(Stats.EAT_CAKE_SLICE);
            player.getFoodStats().addStats(2, 0.1F);
            int i = cakeBlockState.get(BITES);
            if (i < 6) {
                this.cakeBlockState = cakeBlockState.with(BITES, i + 1);
            } else {
                this.getModularEntity().remove(this);
            }

            return ActionResultType.SUCCESS;
        }
    }
}
