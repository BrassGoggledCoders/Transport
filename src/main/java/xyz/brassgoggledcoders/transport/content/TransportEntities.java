package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.ShellMinecart;
import xyz.brassgoggledcoders.transport.entity.TrackLayerMinecart;
import xyz.brassgoggledcoders.transport.renderer.ShellMinecartRenderer;
import xyz.brassgoggledcoders.transport.renderer.TrackLayerMinecartRenderer;

public class TransportEntities {

    public static final EntityEntry<ShellMinecart> SHELL_MINECART = Transport.getRegistrate()
            .object("shell_minecart")
            .<ShellMinecart>entity(ShellMinecart::new, MobCategory.MISC)
            .properties(TransportEntities::minecartProperties)
            .renderer(() -> ShellMinecartRenderer::new)
            .setData(ProviderType.LANG, (context, provider) -> {
                provider.add(context.get().getDescriptionId(), provider.getAutomaticName(context));
                provider.add(context.get().getDescriptionId() + ".with", "Minecart with %s");
            })
            .register();

    public static final EntityEntry<TrackLayerMinecart> TRACK_LAYER_MINECART =  Transport.getRegistrate()
            .object("track_layer")
            .<TrackLayerMinecart>entity(TrackLayerMinecart::new, MobCategory.MISC)
            .properties(TransportEntities::minecartProperties)
            .renderer(() -> TrackLayerMinecartRenderer::new)
            .register();

    public static <T extends AbstractMinecart> void minecartProperties(EntityType.Builder<T> builder) {
        builder.sized(0.98F, 0.7F)
                .clientTrackingRange(8);
    }
    public static void setup() {

    }
}
