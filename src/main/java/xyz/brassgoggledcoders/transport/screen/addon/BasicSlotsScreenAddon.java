package xyz.brassgoggledcoders.transport.screen.addon;

import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.addon.SlotsScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

public class BasicSlotsScreenAddon extends BasicScreenAddon {
    private final IItemHandler itemHandler;
    private final Function<Integer, Pair<Integer, Integer>> positionFunction;

    public BasicSlotsScreenAddon(int posX, int posY, IItemHandler itemHandler,
                                 Function<Integer, Pair<Integer, Integer>> positionFunction) {
        super(posX, posY);
        this.itemHandler = itemHandler;
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
    public void drawBackgroundLayer(MatrixStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int screenX,
                                    int screenY, int mouseX, int mouseY, float partialTicks) {
        SlotsScreenAddon.drawAsset(matrixStack, screen, iAssetProvider, getPosX(), getPosY(), screenX, screenY,
                itemHandler.getSlots(), positionFunction, itemHandler::getStackInSlot, false, slot -> null);
    }

    @Override
    public void drawForegroundLayer(MatrixStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int screenX,
                                    int screenY, int mouseX, int mouseY) {

    }
}
