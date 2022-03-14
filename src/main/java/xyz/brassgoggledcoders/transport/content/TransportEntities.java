package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.ShellMinecart;
import xyz.brassgoggledcoders.transport.renderer.ShellMinecartRenderer;

public class TransportEntities {

    public static final EntityEntry<ShellMinecart> SHELL_MINECART = Transport.getRegistrate()
            .object("shell_minecart")
            .entity(ShellMinecart::new, MobCategory.MISC)
            .renderer(() -> ShellMinecartRenderer::new)
            .register();

    public static void setup() {

    }
}
