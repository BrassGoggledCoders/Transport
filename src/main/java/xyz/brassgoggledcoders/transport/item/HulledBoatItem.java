package xyz.brassgoggledcoders.transport.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.item.IModularItem;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.entity.HulledBoatEntity;
import xyz.brassgoggledcoders.transport.entity.ModularBoatEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Predicate;

public class HulledBoatItem extends GenericBoatItem {
    private final HullType hullType;

    public HulledBoatItem(HullType hullType, Properties properties) {
        super(properties);
        this.hullType = hullType;
    }

    @Nonnull
    @Override
    protected Entity createBoatEntity(ItemStack itemStack, World world, RayTraceResult rayTraceResult) {
        return new HulledBoatEntity(this.getHullType(itemStack), world,  rayTraceResult.getHitVec().x,
                rayTraceResult.getHitVec().y, rayTraceResult.getHitVec().z);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        HullType hullType = this.getHullType(stack);
        if (hullType != null) {
            tooltip.add(new TranslationTextComponent("transport.text.hull_type", hullType.getDisplayName()));
        }
    }

    @Nullable
    private HullType getHullType(ItemStack itemStack) {
        HullType hullType = null;
        CompoundNBT tag = itemStack.getTag();
        if (tag != null && tag.contains("hull_type")) {
            hullType = TransportAPI.HULL_TYPE.get().getValue(new ResourceLocation(tag.getString("hull_type")));
        }
        return hullType;
    }
}
