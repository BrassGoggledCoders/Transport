package xyz.brassgoggledcoders.transport.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.api.item.IHulledItem;
import xyz.brassgoggledcoders.transport.entity.HulledBoatEntity;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class HulledBoatItem extends GenericBoatItem implements IHulledItem {
    private final Supplier<? extends HullType> hullType;

    public HulledBoatItem(Supplier<? extends HullType> hullType, Properties properties) {
        super(properties);
        this.hullType = hullType;
    }

    @Nonnull
    @Override
    protected Entity createBoatEntity(ItemStack itemStack, World world, RayTraceResult rayTraceResult) {
        return new HulledBoatEntity(hullType.get(), world, rayTraceResult.getHitVec().x,
                rayTraceResult.getHitVec().y, rayTraceResult.getHitVec().z);
    }

    @Override
    @Nonnull
    public HullType getHullType(@Nonnull ItemStack itemStack) {
        return hullType.get();
    }
}
