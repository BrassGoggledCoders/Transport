package xyz.brassgoggledcoders.transport.compat.immersiveengineering;

import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.api.tool.RailgunHandler.RailgunRenderColors;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;

public class IEDependentSetup {
    public static void setup() {
        RailgunHandler.registerStandardProjectile(TransportItemTags.RAILS_IRON, 20D, 1.30D)
                .setColorMap(new RailgunRenderColors(0xd8d8d8, 0xd8d8d8, 0xd8d8d8, 0xa8a8a8, 0x686868, 0x686868));
        RailgunHandler.registerStandardProjectile(TransportItemTags.RAILS_GOLD, 20D, 1.30D)
                .setColorMap(new RailgunRenderColors(0xFDF55F, 0xFDF55F, 0xFDF55F, 0xFDF55F, 0xFDF55F, 0xFDF55F));
    }
}
