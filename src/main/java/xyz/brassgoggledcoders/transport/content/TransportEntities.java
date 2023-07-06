package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.MobCategory;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.ShellMinecart;
import xyz.brassgoggledcoders.transport.renderer.ShellMinecartRenderer;

public class TransportEntities {

    public static final EntityEntry<ShellMinecart> SHELL_MINECART = Transport.getRegistrate()
            .object("shell_minecart")
            .<ShellMinecart>entity(ShellMinecart::new, MobCategory.MISC)
            .renderer(() -> ShellMinecartRenderer::new)
            .setData(ProviderType.LANG, (context, provider) -> {
                provider.add(context.get().getDescriptionId(), provider.getAutomaticName(context, Registry.ENTITY_TYPE_REGISTRY));
                provider.add(context.get().getDescriptionId() + ".with", "Minecart with %s");
            })
            .register();

    public static void setup() {

    }
}
