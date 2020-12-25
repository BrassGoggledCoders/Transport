package xyz.brassgoggledcoders.transport.item;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;
import xyz.brassgoggledcoders.transport.entity.LocomotiveEntity;

import javax.annotation.Nonnull;

public class LocomotiveItem extends BasicMinecartItem {
    private final NonNullSupplier<EntityType<LocomotiveEntity>> entityTypeSupplier;

    public LocomotiveItem(NonNullSupplier<EntityType<LocomotiveEntity>> entityTypeSupplier, Properties builder) {
        super(builder);
        this.entityTypeSupplier = entityTypeSupplier;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        return armorType == EquipmentSlotType.HEAD || armorType.getSlotType() == EquipmentSlotType.Group.HAND;
    }

    @Override
    @Nonnull
    protected AbstractMinecartEntity create(ItemUseContext itemUseContext, double heightOffset) {
        BlockPos blockPos = itemUseContext.getPos();
        LocomotiveEntity locomotiveEntity = new LocomotiveEntity(entityTypeSupplier.get(), itemUseContext.getWorld(),
                (double) blockPos.getX() + 0.5D, blockPos.getY() + 0.0625D + heightOffset,
                (double) blockPos.getZ() + 0.5D);

        locomotiveEntity.onPlaced(itemUseContext);

        return locomotiveEntity;
    }
}
