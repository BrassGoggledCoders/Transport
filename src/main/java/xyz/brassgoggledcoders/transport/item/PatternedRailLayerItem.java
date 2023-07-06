package xyz.brassgoggledcoders.transport.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.capability.IRailProvider;
import xyz.brassgoggledcoders.transport.capability.CapabilityProvider;
import xyz.brassgoggledcoders.transport.capability.PatternedRailProvider;
import xyz.brassgoggledcoders.transport.menu.PatternedRailLayerMenu;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

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

    @Override
    @NotNull
    public InteractionResult useOn(@NotNull UseOnContext pContext) {
        ItemStack heldStack = pContext.getItemInHand();
        LazyOptional<IRailProvider> railProvider = heldStack.getCapability(IRailProvider.CAPABILITY);
        ItemStack railStack = Optional.ofNullable(pContext.getPlayer())
                .flatMap(livingEntity -> livingEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .resolve()
                ).flatMap(inventory -> railProvider.map(capability -> capability.findNext(inventory, false))
                ).orElse(ItemStack.EMPTY);

        if (railStack.is(ItemTags.RAILS)) {
            InteractionResult result = railStack.useOn(new UseOnContext(
                    pContext.getLevel(),
                    pContext.getPlayer(),
                    pContext.getHand(),
                    railStack,
                    new BlockHitResult(
                            pContext.getClickLocation(),
                            pContext.getClickedFace(),
                            pContext.getClickedPos(),
                            pContext.isInside()
                    )
            ));
            if (result.consumesAction()) {
                railProvider.ifPresent(IRailProvider::nextPosition);
            }
            return result;
        }

        return super.useOn(pContext);
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
                    if (railProvider instanceof PatternedRailProvider patternedRailProvider) {
                        return new PatternedRailLayerMenu(
                                pContainerId,
                                pInventory,
                                patternedRailProvider.getPattern(),
                                patternedRailProvider.getPosition()
                        );
                    } else {
                        return null;
                    }
                })
                .orElse(null);
    }
}
