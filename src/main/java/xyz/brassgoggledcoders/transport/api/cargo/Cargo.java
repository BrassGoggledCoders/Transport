package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class Cargo extends ForgeRegistryEntry<Cargo> {
    private final Supplier<? extends Block> blockSupplier;
    private BlockState blockState;
    private String translationKey = null;
    private ITextComponent name;

    public Cargo(Supplier<? extends Block> blockSupplier) {
        this.blockSupplier = blockSupplier;
    }


    public CargoInstance create(@Nullable World world) {
        return new CargoInstance(this);
    }

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.makeTranslationKey("cargo", this.getRegistryName());
        }
        return translationKey;
    }

    public ITextComponent getName() {
        if (name == null) {
            name = new TranslationTextComponent(this.getTranslationKey());
        }
        return name;
    }

    public BlockState getDefaultBlockState() {
        if (blockState == null) {
            blockState = blockSupplier.get().getDefaultState();
        }
        return blockState;
    }
}
