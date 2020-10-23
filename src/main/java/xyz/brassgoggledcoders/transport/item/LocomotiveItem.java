package xyz.brassgoggledcoders.transport.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.entity.LocomotiveEntity;

import javax.annotation.Nonnull;

public class LocomotiveItem extends BasicMinecartItem {
    public LocomotiveItem(Properties builder) {
        super(builder);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        return armorType == EquipmentSlotType.HEAD || armorType.getSlotType() == EquipmentSlotType.Group.HAND;
    }

    @Override
    @Nonnull
    protected AbstractMinecartEntity create(ItemUseContext itemUseContext, double heightOffset) {
        BlockPos blockPos = itemUseContext.getPos();
        return new LocomotiveEntity(TransportEntities.DIESEL_LOCOMOTIVE.get(), itemUseContext.getWorld(),
                (double) blockPos.getX() + 0.5D, blockPos.getY() + 0.0625D + heightOffset,
                (double) blockPos.getZ() + 0.5D);
    }
}
