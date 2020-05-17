package xyz.brassgoggledcoders.transport.screen.addon;

import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.SlotsScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.client.gui.screen.Screen;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

public class BasicSlotsScreenAddon extends BasicScreenAddon {
    private final int slots;
    private final Function<Integer, Pair<Integer, Integer>> positionFunction;

    public BasicSlotsScreenAddon(int posX, int posY, int slots, Function<Integer, Pair<Integer, Integer>> positionFunction) {
        super(posX, posY);
        this.slots = slots;
        this.positionFunction = positionFunction;
    }

    @Override
    public int getXSize() {
        return 0;
    }

    @Override
    public int getYSize() {
        return 0;
    }

    @Override
    public void drawBackgroundLayer(Screen screen, IAssetProvider iAssetProvider, int screenX, int screenY, int mouseX,
                                    int mouseY, float partialTicks) {
        SlotsScreenAddon.drawAsset(screen, iAssetProvider, screenX, screenY, this.getPosX(), this.getPosY(), slots,
                positionFunction, false, -1);
    }

    @Override
    public void drawForegroundLayer(Screen screen, IAssetProvider iAssetProvider, int screenX, int screenY, int mouseX,
                                    int mouseY) {

    }
}
