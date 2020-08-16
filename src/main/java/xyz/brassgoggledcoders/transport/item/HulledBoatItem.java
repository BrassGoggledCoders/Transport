package xyz.brassgoggledcoders.transport.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.entity.HulledBoatEntity;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class HulledBoatItem extends GenericBoatItem {
    private final Supplier<HullType> hullType;

    public HulledBoatItem(Supplier<HullType> hullType, Properties properties) {
        super(properties);
        this.hullType = hullType;
    }

    @Nonnull
    @Override
    protected Entity createBoatEntity(ItemStack itemStack, World world, RayTraceResult rayTraceResult) {
        return new HulledBoatEntity(hullType.get(), world, rayTraceResult.getHitVec().x,
                rayTraceResult.getHitVec().y, rayTraceResult.getHitVec().z);
    }
}
