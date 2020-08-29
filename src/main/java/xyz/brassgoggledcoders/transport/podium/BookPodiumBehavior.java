package xyz.brassgoggledcoders.transport.podium;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.LecternContainer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.brassgoggledcoders.transport.api.podium.IPodium;
import xyz.brassgoggledcoders.transport.api.podium.PodiumBehavior;
import xyz.brassgoggledcoders.transport.container.reference.SingleIntArray;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BookPodiumBehavior extends PodiumBehavior {
    private SingleIntArray page;

    public BookPodiumBehavior(IPodium podium) {
        super(podium);
        this.page = new SingleIntArray();
    }

    @Override
    public ActionResultType activate(PlayerEntity playerEntity) {
        if (playerEntity instanceof ServerPlayerEntity) {
            playerEntity.openContainer(new LecternContainerProvider());
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("page", page.get(0));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        page.set(0, nbt.getInt("page"));
    }

    public class LecternContainerProvider implements INamedContainerProvider {
        @Override
        @Nonnull
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("container.lectern");
        }

        @Nullable
        @Override
        @ParametersAreNonnullByDefault
        public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            return new LecternContainer(id, BookPodiumBehavior.super.getPodium().getPodiumInventory(), page);
        }
    }
}
