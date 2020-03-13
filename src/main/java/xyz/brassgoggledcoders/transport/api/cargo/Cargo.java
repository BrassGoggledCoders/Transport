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
import org.lwjgl.system.CallbackI;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class Cargo extends ForgeRegistryEntry<Cargo> implements IItemProvider {
    private final Supplier<? extends Block> blockSupplier;
    private final Function<Cargo,? extends CargoInstance> cargoInstanceCreator;
    private BlockState blockState;
    private String translationKey = null;
    private ITextComponent name;

    public Cargo(Supplier<? extends Block> blockSupplier) {
        this(blockSupplier, CargoInstance::new);
    }

    public Cargo(Supplier<? extends Block> blockSupplier, Function<Cargo, ? extends CargoInstance> cargoInstanceCreator) {
        this.blockSupplier = blockSupplier;
        this.cargoInstanceCreator = cargoInstanceCreator;
    }

    @Nonnull
    public CargoInstance create(@Nullable World world) {
        return cargoInstanceCreator.apply(this);
    }

    @Nonnull
    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.makeTranslationKey("cargo", this.getRegistryName());
        }
        return translationKey;
    }

    @Nonnull
    public ITextComponent getDisplayName() {
        if (name == null) {
            name = new TranslationTextComponent(this.getTranslationKey());
        }
        return name;
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
