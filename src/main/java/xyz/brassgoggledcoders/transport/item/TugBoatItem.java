package xyz.brassgoggledcoders.transport.item;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.entity.TugBoatEntity;

import javax.annotation.Nonnull;

public class TugBoatItem<T extends TugBoatEntity> extends GenericBoatItem {
    private final NonNullSupplier<EntityType<T>> entityTypeSupplier;

    public TugBoatItem(NonNullSupplier<EntityType<T>> entityTypeSupplier, Properties properties) {
        super(properties);
        this.entityTypeSupplier = entityTypeSupplier;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        return armorType == EquipmentSlotType.HEAD || armorType.getSlotType() == EquipmentSlotType.Group.HAND;
    }

    @Nonnull
    @Override
    protected Entity createBoatEntity(ItemStack itemStack, World world, RayTraceResult rayTraceResult) {
        return new TugBoatEntity(entityTypeSupplier.get(), world, rayTraceResult.getHitVec().x,
                rayTraceResult.getHitVec().y, rayTraceResult.getHitVec().z);
    }
}
