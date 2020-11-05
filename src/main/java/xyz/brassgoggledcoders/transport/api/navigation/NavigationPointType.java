package xyz.brassgoggledcoders.transport.api.navigation;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
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
    private final BiFunction<INavigationNetwork, NavigationPointType, NavigationPoint> instanceSupplier;

    public NavigationPointType(BiFunction<INavigationNetwork, NavigationPointType, NavigationPoint> instanceSupplier) {
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

    public NavigationPoint create(INavigationNetwork navigationNetwork) {
        return this.instanceSupplier.apply(navigationNetwork, this);
    }

    public static NavigationPointType of(BiFunction<INavigationNetwork, NavigationPointType, NavigationPoint> instanceSupplier) {
        return new NavigationPointType(instanceSupplier);
    }

    @Nullable
    public static INavigationPoint deserialize(INavigationNetwork navigationNetwork, CompoundNBT nbt) {
        NavigationPointType pointType = TransportRegistries.NAVIGATION_POINT_TYPES.getValue(
                new ResourceLocation(nbt.getString("type")));

        if (pointType != null) {
            NavigationPoint navigationPoint = pointType.create(navigationNetwork);
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
