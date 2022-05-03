package xyz.brassgoggledcoders.transport.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.menu.PatternedRailLayerMenu;
import xyz.brassgoggledcoders.transport.api.capability.IRailProvider;
import xyz.brassgoggledcoders.transport.capability.CapabilityProvider;
import xyz.brassgoggledcoders.transport.capability.PatternedRailProvider;

import javax.annotation.ParametersAreNonnullByDefault;

public class PatternedRailLayerItem extends Item implements MenuProvider {
    public PatternedRailLayerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand == InteractionHand.MAIN_HAND && pPlayer.isCrouching()) {
            pPlayer.openMenu(this);
            return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide());
        } else {
            return super.use(pLevel, pPlayer, pUsedHand);
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new CapabilityProvider<>(
                IRailProvider.CAPABILITY,
                PatternedRailProvider::new,
                PatternedRailProvider::fromTag,
                PatternedRailProvider::toTag
        );
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return this.getDescription();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return pPlayer.getItemInHand(InteractionHand.MAIN_HAND)
                .getCapability(IRailProvider.CAPABILITY)
                .resolve()
                .map(railProvider -> {
                    if (railProvider instanceof PatternedRailProvider) {
                        return new PatternedRailLayerMenu(pContainerId, pInventory, ((PatternedRailProvider) railProvider).getPattern());
                    } else {
                        return null;
                    }
                })
                .orElse(null);
    }
}
