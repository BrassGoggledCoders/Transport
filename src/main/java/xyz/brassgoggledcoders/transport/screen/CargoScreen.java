package xyz.brassgoggledcoders.transport.screen;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.client.screen.container.BasicContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;
import xyz.brassgoggledcoders.transport.container.cargo.CargoContainer;

public class CargoScreen extends BasicContainerScreen<CargoContainer> {
    public CargoScreen(CargoContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        CargoInstance cargoInstance = container.getCargoInstance();
        if (cargoInstance instanceof IScreenAddonProvider) {
            ((IScreenAddonProvider) cargoInstance).getScreenAddons()
                    .stream()
                    .map(IFactory::create)
                    .forEach(this.getAddons()::add);
        }
    }
}
