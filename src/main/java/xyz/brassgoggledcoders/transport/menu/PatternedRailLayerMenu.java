package xyz.brassgoggledcoders.transport.menu;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.content.TransportItems;
import xyz.brassgoggledcoders.transport.menu.slot.TagFilterSlot;
import xyz.brassgoggledcoders.transport.shellcontent.storage.item.CapabilityContainer;

public class PatternedRailLayerMenu extends ChestMenu {

    private final DataSlot positionData = DataSlot.standalone();
    public PatternedRailLayerMenu(MenuType<?> menuType, int windowId, Inventory inventory) {
        super(menuType, windowId, inventory, new SimpleContainer(9), 1);
        this.addDataSlot(positionData);
    }

    public PatternedRailLayerMenu(int windowId, Inventory inventory, IItemHandlerModifiable itemHandler, int position) {
        super(TransportContainers.PATTERNED_RAIL_LAYER.get(), windowId, inventory,
                new CapabilityContainer(itemHandler, PatternedRailLayerMenu::isItValid), 1);
        positionData.set(position);
        this.addDataSlot(positionData);
    }

    @Override
    @NotNull
    protected Slot addSlot(Slot pSlot) {
        if (pSlot.container instanceof CapabilityContainer) {
            return super.addSlot(new TagFilterSlot(pSlot.container, pSlot.getSlotIndex(), pSlot.x, pSlot.y, ItemTags.RAILS));
        } else {
            return super.addSlot(pSlot);
        }
    }

    public int getPosition() {
        return positionData.get();
    }

    private static boolean isItValid(Player player) {
        return player.isAlive() && TransportItems.PATTERNED_RAIL_LAYER.isIn(player.getItemInHand(InteractionHand.MAIN_HAND));
    }
}
