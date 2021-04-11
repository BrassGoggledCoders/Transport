package xyz.brassgoggledcoders.transport.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.NoteBlockInstrument;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateStorageProvider;
import xyz.brassgoggledcoders.transport.capability.itemhandler.furnaceminecart.FurnaceMinecartFuelProvider;
import xyz.brassgoggledcoders.transport.content.TransportSounds;

@EventBusSubscriber(modid = Transport.ID, bus = EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    public static final ResourceLocation FURNACE_FUEL = Transport.rl("furnace_fuel");
    public static final ResourceLocation PREDICATE_STORAGE = Transport.rl("predicate_storage");

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> entityAttachCapabilitiesEvent) {
        if (entityAttachCapabilitiesEvent.getObject() instanceof FurnaceMinecartEntity) {
            FurnaceMinecartFuelProvider fuelProvider = new FurnaceMinecartFuelProvider(
                    (FurnaceMinecartEntity) entityAttachCapabilitiesEvent.getObject());
            entityAttachCapabilitiesEvent.addCapability(FURNACE_FUEL, fuelProvider);
            entityAttachCapabilitiesEvent.addListener(fuelProvider::invalidate);
        }
    }

    @SubscribeEvent
    public static void onAttachTileEntityCapabilities(AttachCapabilitiesEvent<TileEntity> attachCapabilitiesEvent) {
        if (attachCapabilitiesEvent.getObject() instanceof LecternTileEntity) {
            PredicateStorageProvider storageProvider = new PredicateStorageProvider();
            attachCapabilitiesEvent.addCapability(PREDICATE_STORAGE, storageProvider);
            attachCapabilitiesEvent.addListener(storageProvider::invalidate);
        }
    }

    @SubscribeEvent
    public static void onNoteBlockPlayer(NoteBlockEvent.Play playEvent) {
        if (playEvent.getInstrument().equals(NoteBlockInstrument.HARP) && playEvent.getWorld().getBlockState(playEvent.getPos().down()).isIn(BlockTags.RAILS)) {
            playEvent.setCanceled(true);
            int i = playEvent.getVanillaNoteId();
            float f = (float) Math.pow(2.0D, (double) (i - 12) / 12.0D);
            BlockPos pos = playEvent.getPos();
            if (playEvent.getWorld() instanceof ServerWorld) {
                playEvent.getWorld().playSound(null, pos, TransportSounds.WHISTLE.get(), SoundCategory.RECORDS, 3.0F, f);
                ((ServerWorld) playEvent.getWorld()).spawnParticle(
                        ParticleTypes.NOTE,
                        (double) pos.getX() + 0.5D,
                        (double) pos.getY() + 1.2D,
                        (double) pos.getZ() + 0.5D,
                        1,
                        0.0D,
                        1.0D,
                        0.0D,
                        (double) i / 24.0D
                );
            }
        }
    }
}
