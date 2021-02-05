package xyz.brassgoggledcoders.transport.item.locomotive;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;
import xyz.brassgoggledcoders.transport.entity.locomotive.LocomotiveEntity;
import xyz.brassgoggledcoders.transport.item.BasicMinecartItem;

import javax.annotation.Nullable;

public class LocomotiveItem<T extends LocomotiveEntity<?>> extends BasicMinecartItem {
    private final NonNullSupplier<EntityType<T>> entityTypeSupplier;

    public LocomotiveItem(NonNullSupplier<EntityType<T>> entityTypeSupplier, Properties builder) {
        super(builder);
        this.entityTypeSupplier = entityTypeSupplier;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        return armorType == EquipmentSlotType.HEAD || armorType.getSlotType() == EquipmentSlotType.Group.HAND;
    }

    @Override
    @Nullable
    protected AbstractMinecartEntity create(ItemUseContext itemUseContext, double heightOffset) {
        BlockPos blockPos = itemUseContext.getPos();
        T locomotiveEntity = entityTypeSupplier.get().create(itemUseContext.getWorld());
        if (locomotiveEntity != null) {
            locomotiveEntity.setPosition(
                    (double) blockPos.getX() + 0.5D,
                    blockPos.getY() + 0.0625D + heightOffset,
                    (double) blockPos.getZ() + 0.5D
            );

            locomotiveEntity.onPlaced(itemUseContext);
            locomotiveEntity.readFromItemStack(itemUseContext.getItem());
        }
        return locomotiveEntity;
    }
}
