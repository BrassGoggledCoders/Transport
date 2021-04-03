package xyz.brassgoggledcoders.transport.compat.vanilla.module.cargo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.NoteBlockInstrument;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.content.TransportSounds;

import java.util.Objects;

public class NoteBlockCargoModuleInstance extends CargoModuleInstance {
    private SoundEvent soundEvent = NoteBlockInstrument.HARP.getSound();
    private int note = 0;
    private int lastNote = 0;

    public NoteBlockCargoModuleInstance(CargoModule cargoModule, IModularEntity modularEntity) {
        super(cargoModule, modularEntity);
    }

    @Override
    public void tick() {
        super.tick();
        if (lastNote > 0) {
            lastNote--;
        }
    }

    @Override
    public ActionResultType applyInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (heldItem.getItem() instanceof BlockItem) {
            BlockState blockState = ((BlockItem) heldItem.getItem()).getBlock().getDefaultState();
            SoundEvent newSoundEvent;
            if (blockState.isIn(BlockTags.RAILS)) {
                newSoundEvent = TransportSounds.WHISTLE.get();
            } else {
                newSoundEvent = NoteBlockInstrument.byState(blockState).getSound();
            }
            if (newSoundEvent != soundEvent) {
                soundEvent = newSoundEvent;
            } else {
                note++;
                if (note > 24) {
                    note = 0;
                }
            }
            return ActionResultType.SUCCESS;
        } else {
            note++;
            if (note > 24) {
                note = 0;
            }
        }

        return super.applyInteraction(player, vec, hand);
    }

    @Override
    public void onActivatorPass(boolean receivingPower) {
        if (receivingPower && lastNote <= 0) {
            int i = note;
            float f = (float) Math.pow(2.0D, (double) (i - 12) / 12.0D);
            BlockPos pos = this.getModularEntity().getSelf().getPosition();
            this.getWorld().playSound(null, pos, soundEvent, SoundCategory.RECORDS, 3.0F, f);
            this.getWorld().addParticle(ParticleTypes.NOTE, (double) pos.getX() + 0.5D, (double) pos.getY() + 1.6D, (double) pos.getZ() + 0.5D, (double) i / 24.0D, 0.0D, 0.0D);
        }
        lastNote = 20;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("note", this.note);
        nbt.putString("soundEvent", Objects.requireNonNull(this.soundEvent.getRegistryName()).toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.note = nbt.getInt("note");
        this.soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(nbt.getString("soundEvent")));
        if (this.soundEvent == null) {
            this.soundEvent = NoteBlockInstrument.HARP.getSound();
        }
    }
}
