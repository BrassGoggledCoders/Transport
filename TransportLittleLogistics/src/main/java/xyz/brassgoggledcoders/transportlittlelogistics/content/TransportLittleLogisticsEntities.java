package xyz.brassgoggledcoders.transportlittlelogistics.content;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;
import xyz.brassgoggledcoders.transportlittlelogistics.TransportLittleLogistics;
import xyz.brassgoggledcoders.transportlittlelogistics.entity.ShellWagon;
import xyz.brassgoggledcoders.transportlittlelogistics.renderer.ShellWagonRenderer;

public class TransportLittleLogisticsEntities {

    public static final EntityEntry<ShellWagon> SHELL_WAGON = TransportLittleLogistics.getRegistrate()
            .object("shell_wagon")
            .<ShellWagon>entity(ShellWagon::new, MobCategory.MISC)
            .renderer(() -> ShellWagonRenderer::new)
            .setData(ProviderType.LANG, (context, provider) -> {
                provider.add(context.get().getDescriptionId(), provider.getAutomaticName(context));
                provider.add(context.get().getDescriptionId() + ".with", "Train Car with %s");
            })
            .register();

    public static void setup() {

    }
}
