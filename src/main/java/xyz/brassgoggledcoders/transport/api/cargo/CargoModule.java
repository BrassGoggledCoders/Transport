package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.NonNullLazy;
import xyz.brassgoggledcoders.transport.api.TransportObjects;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class CargoModule extends Module<CargoModule> {
    private final Supplier<? extends Block> blockSupplier;
    private BlockState blockState;

    private final NonNullLazy<Boolean> isActive;

    public CargoModule(Supplier<? extends Block> blockSupplier) {
        this(blockSupplier, CargoModuleInstance::new);
    }

    public CargoModule(Supplier<? extends Block> blockSupplier, BiFunction<CargoModule, IModularEntity, ? extends CargoModuleInstance> cargoInstanceCreator) {
        super(TransportObjects.CARGO_TYPE, cargoInstanceCreator);
        this.blockSupplier = blockSupplier;
        this.isActive = NonNullLazy.of(() -> blockSupplier.get() != Blocks.AIR);
    }

    @Nonnull
    public BlockState getDefaultBlockState() {
        if (blockState == null) {
            blockState = blockSupplier.get().getDefaultState();
        }
        return blockState;
    }

    public boolean isActive() {
        return this.isActive.get();
    }

    public ItemStack createItemStack(Item item) {
        return this.createItemStack(item, null);
    }

    public ItemStack createItemStack(Item item, @Nullable CargoModuleInstance cargoModuleInstance) {
        ItemStack itemStack = new ItemStack(item);
        CompoundNBT cargoNBT = itemStack.getOrCreateChildTag("cargo");
        cargoNBT.putString("name", String.valueOf(this.getRegistryName()));
        if (cargoModuleInstance != null) {
            cargoNBT.put("instance", cargoModuleInstance.serializeNBT());
        }
        return itemStack;
    }

    @Override
    @Nonnull
    public Item asItem() {
        return this.getDefaultBlockState().getBlock().asItem();
    }
}
