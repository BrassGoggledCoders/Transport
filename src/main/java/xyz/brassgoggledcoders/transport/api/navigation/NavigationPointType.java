package xyz.brassgoggledcoders.transport.api.navigation;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.transport.api.TransportRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiFunction;

public class NavigationPointType extends ForgeRegistryEntry<NavigationPointType> {
    private ITextComponent displayName;
    private String translationKey;
    private final BiFunction<NavigationPointType, BlockPos, NavigationPoint> instanceSupplier;

    public NavigationPointType(BiFunction<NavigationPointType, BlockPos, NavigationPoint> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    public String getTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("navigation_point", this.getRegistryName());
        }
        return this.translationKey;
    }

    public ITextComponent getDisplayName() {
        if (this.displayName == null) {
            this.displayName = new TranslationTextComponent(this.getTranslationKey());
        }
        return this.displayName;
    }

    public NavigationPoint create(BlockPos blockPos) {
        return this.instanceSupplier.apply(this, blockPos);
    }

    public static NavigationPointType of(BiFunction<NavigationPointType, BlockPos, NavigationPoint> instanceSupplier) {
        return new NavigationPointType(instanceSupplier);
    }

    @Nullable
    public static INavigationPoint deserialize(CompoundNBT nbt) {
        NavigationPointType pointType = TransportRegistries.NAVIGATION_POINT_TYPES.getValue(
                new ResourceLocation(nbt.getString("type")));

        BlockPos blockPos = NBTUtil.readBlockPos(nbt.getCompound("blockPos"));

        if (pointType != null) {
            NavigationPoint navigationPoint = pointType.create(blockPos);
            navigationPoint.deserializeNBT(nbt);
            return navigationPoint;
        } else {
            return null;
        }
    }

    @Nonnull
    public static CompoundNBT serialize(@Nonnull INavigationPoint navigationPoint) {
        CompoundNBT nbt = navigationPoint.serializeNBT();
        nbt.putString("type", Objects.requireNonNull(navigationPoint.getType().getRegistryName()).toString());
        nbt.putUniqueId("uniqueId", navigationPoint.getUniqueId());
        nbt.put("blockPos", NBTUtil.writeBlockPos(navigationPoint.getPosition()));
        return nbt;
    }

}
