package xyz.brassgoggledcoders.transport.content;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.MobCategory;
import xyz.brassgoggledcoders.shadyskies.registering.entity.EntityEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.ShellMinecart;
import xyz.brassgoggledcoders.transport.renderer.ShellMinecartRenderer;

public class TransportEntities {

    public static final EntityEntry<ShellMinecart> SHELL_MINECART = Transport.getRegistering()
            .object("shell_minecart")
            .<ShellMinecart>entity(ShellMinecart::new)
            .withCategory(MobCategory.MISC)
            .withBuilder(properties -> properties.sized(0.98F, 0.7F)
                    .clientTrackingRange(8)
            )
            .withRenderer(() -> ShellMinecartRenderer::new)
            .register();

    public static void setup() {

    }
}
