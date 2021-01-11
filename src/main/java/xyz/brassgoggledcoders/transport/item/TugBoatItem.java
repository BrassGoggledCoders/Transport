package xyz.brassgoggledcoders.transport.item;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.content.TransportText;
import xyz.brassgoggledcoders.transport.entity.TugBoatEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

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

    @Override
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, worldIn, tooltip, flag);
        tooltip.add(TransportText.NOT_YET_IMPLEMENTED);
    }
}
