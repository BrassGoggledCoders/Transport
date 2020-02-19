package xyz.brassgoggledcoders.transport.screen;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.client.screen.container.BasicContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.container.LoaderContainer;

public class LoaderScreen extends BasicContainerScreen<LoaderContainer> {
    public LoaderScreen(LoaderContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.getContainer()
                .getLoader()
                .getScreenAddons()
                .stream()
                .map(IFactory::create)
                .forEach(this.getAddons()::add);
    }
}
