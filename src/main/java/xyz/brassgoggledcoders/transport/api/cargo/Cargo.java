package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.transport.api.TransportObjects;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Cargo extends Module<Cargo> implements IItemProvider {
    private final Supplier<? extends Block> blockSupplier;
    private BlockState blockState;

    public Cargo(Supplier<? extends Block> blockSupplier) {
        this(blockSupplier, CargoInstance::new);
    }

    public Cargo(Supplier<? extends Block> blockSupplier, BiFunction<Cargo, IModularEntity, ? extends CargoInstance> cargoInstanceCreator) {
        super(TransportObjects.CARGO_TYPE, cargoInstanceCreator);
        this.blockSupplier = blockSupplier;
    }

    @Nonnull
    public BlockState getDefaultBlockState() {
        if (blockState == null) {
            blockState = blockSupplier.get().getDefaultState();
        }
        return blockState;
    }

    public boolean isEmpty() {
        return false;
    }

    public ItemStack createItemStack(Item item) {
        return this.createItemStack(item, null);
    }

    public ItemStack createItemStack(Item item, @Nullable CargoInstance cargoInstance) {
        ItemStack itemStack = new ItemStack(item);
        CompoundNBT cargoNBT = itemStack.getOrCreateChildTag("cargo");
        cargoNBT.putString("name", String.valueOf(this.getRegistryName()));
        if (cargoInstance != null) {
            cargoNBT.put("instance", cargoInstance.serializeNBT());
        }
        return itemStack;
    }

    @Override
    @Nonnull
    public Item asItem() {
        return this.getDefaultBlockState().getBlock().asItem();
    }
}
