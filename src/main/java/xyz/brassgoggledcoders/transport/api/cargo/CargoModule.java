package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import xyz.brassgoggledcoders.transport.api.TransportObjects;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class CargoModule extends Module<CargoModule> {
    private final NonNullLazy<? extends BlockState> blockStateLazy;
    private final NonNullLazy<? extends Item> itemLazy;

    private final NonNullLazy<Boolean> isActive;
    private final boolean useContentTranslation;

    public CargoModule(Supplier<? extends Block> blockLazy) {
        this(blockLazy, CargoModuleInstance::new);
    }

    public CargoModule(Supplier<? extends Block> blockSupplier, BiFunction<CargoModule, IModularEntity,
            ? extends CargoModuleInstance> cargoInstanceCreator) {
        this(blockSupplier, cargoInstanceCreator, false);
    }

    public CargoModule(Supplier<? extends Block> blockSupplier, BiFunction<CargoModule, IModularEntity,
            ? extends CargoModuleInstance> cargoInstanceCreator, boolean useContentTranslation) {
        this(() -> blockSupplier.get().getDefaultState(), () -> blockSupplier.get() == null ? Items.AIR :
                blockSupplier.get().asItem(), cargoInstanceCreator, useContentTranslation);
    }

    public CargoModule(Supplier<? extends BlockState> blockSupplier, Supplier<? extends Item> itemSupplier, BiFunction<CargoModule, IModularEntity,
            ? extends CargoModuleInstance> cargoInstanceCreator, boolean useContentTranslation) {
        super(TransportObjects.CARGO_TYPE, cargoInstanceCreator);
        this.blockStateLazy = NonNullLazy.of(() -> blockSupplier.get() == null ? Blocks.AIR.getDefaultState() :
                blockSupplier.get());
        this.itemLazy = NonNullLazy.of(() -> itemSupplier.get() == null ? Items.AIR : itemSupplier.get());
        this.isActive = NonNullLazy.of(() -> blockSupplier.get() != null || itemSupplier.get() != null);
        this.useContentTranslation = useContentTranslation;
    }

    @Nonnull
    public BlockState getDefaultBlockState() {
        return this.blockStateLazy.get();
    }

    public boolean isActive() {
        return this.isActive.get();
    }

    @Nonnull
    @Override
    public String getTranslationKey() {
        return useContentTranslation ? this.itemLazy.get().getTranslationKey() : super.getTranslationKey();
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
        return this.itemLazy.get();
    }

    public static CargoModule fromItem(NonNullSupplier<? extends Item> itemSupplier, BiFunction<CargoModule, IModularEntity,
            ? extends CargoModuleInstance> cargoInstanceCreator) {
        return fromItem(itemSupplier, cargoInstanceCreator, false);
    }

    public static CargoModule fromItem(NonNullSupplier<? extends Item> itemSupplier, BiFunction<CargoModule, IModularEntity,
            ? extends CargoModuleInstance> cargoInstanceCreator, boolean useContentTranslation) {
        return new CargoModule(() -> null, itemSupplier::get, cargoInstanceCreator, useContentTranslation);
    }

    public static CargoModule fromBlockState(NonNullSupplier<? extends BlockState> blockStateSupplier,
                                             BiFunction<CargoModule, IModularEntity, ? extends CargoModuleInstance> cargoInstanceCreator,
                                             boolean useContentTranslation) {
        return new CargoModule(blockStateSupplier::get, () -> blockStateSupplier.get().getBlock().asItem(),
                cargoInstanceCreator, useContentTranslation);
    }

    public static CargoModule fromBlockName(ResourceLocation name,
                                            BiFunction<CargoModule, IModularEntity, ? extends CargoModuleInstance> cargoInstanceCreator,
                                            boolean useContentTranslation) {
        RegistryObject<Block> registryObject = RegistryObject.of(name, ForgeRegistries.BLOCKS);
        return new CargoModule(() -> registryObject.orElse(Blocks.AIR), cargoInstanceCreator, useContentTranslation);
    }
}
