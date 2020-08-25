package xyz.brassgoggledcoders.transport.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
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
import xyz.brassgoggledcoders.transport.content.TransportHullTypes;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;
import xyz.brassgoggledcoders.transport.entity.ModularBoatEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ModularBoatItem extends Item implements IModularItem<ModularBoatEntity> {
    private static final Predicate<Entity> CAN_COLLIDE = EntityPredicates.NOT_SPECTATING.and(Entity::canBeCollidedWith);

    public ModularBoatItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        RayTraceResult rayTraceResult = rayTrace(world, player, RayTraceContext.FluidMode.ANY);
        if (rayTraceResult.getType() == RayTraceResult.Type.MISS) {
            return ActionResult.resultPass(itemStack);
        } else {
            Vector3d vector3d = player.getLook(1.0F);
            List<Entity> list = world.getEntitiesInAABBexcluding(player, player.getBoundingBox()
                    .expand(vector3d.scale(5.0D))
                    .grow(1.0D), CAN_COLLIDE);

            if (!list.isEmpty()) {
                Vector3d vector3d1 = player.getEyePosition(1.0F);

                for (Entity entity : list) {
                    AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(entity.getCollisionBorderSize());
                    if (axisalignedbb.contains(vector3d1)) {
                        return ActionResult.resultPass(itemStack);
                    }
                }
            }

            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                Entity boatEntity = createBoatEntity(itemStack, world, rayTraceResult);
                boatEntity.rotationYaw = player.rotationYaw;
                if (!world.hasNoCollisions(boatEntity, boatEntity.getBoundingBox().grow(-0.1D))) {
                    return ActionResult.resultFail(itemStack);
                } else {
                    if (!world.isRemote) {
                        world.addEntity(boatEntity);
                        if (!player.abilities.isCreativeMode) {
                            itemStack.shrink(1);
                        }
                    }

                    player.addStat(Stats.ITEM_USED.get(this));
                    return ActionResult.func_233538_a_(itemStack, world.isRemote());
                }
            } else {
                return ActionResult.resultPass(itemStack);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            if (group == ItemGroup.SEARCH) {
                CompoundNBT tagNBT = new CompoundNBT();
                tagNBT.putString("hull_type", TransportHullTypes.OAK.getId().toString());
                ItemStack itemStack = new ItemStack(this);
                itemStack.setTag(tagNBT);
                items.add(itemStack);
            } else {
                for (Map.Entry<ResourceLocation, HullType> hullType : TransportAPI.HULL_TYPE.get().getEntries()) {
                    Item hullItem = hullType.getValue().asItem();
                    if (hullItem.isIn(TransportItemTags.BOAT_HULL)) {
                        CompoundNBT tagNBT = new CompoundNBT();
                        tagNBT.putString("hull_type", hullType.getKey().toString());
                        ItemStack itemStack = new ItemStack(this);
                        itemStack.setTag(tagNBT);
                        items.add(itemStack);
                    }
                }
            }
        }
    }

    @Nonnull
    private Entity createBoatEntity(ItemStack itemStack, World world, RayTraceResult rayTraceResult) {
        ModularBoatEntity modularBoatEntity = new ModularBoatEntity(world, rayTraceResult.getHitVec().x,
                rayTraceResult.getHitVec().y, rayTraceResult.getHitVec().z);
        if (itemStack.hasDisplayName()) {
            modularBoatEntity.setCustomName(itemStack.getDisplayName());
        }
        modularBoatEntity.setHullType(this.getHullType(itemStack.getTag()));
        LazyOptional<IModularEntity> modularEntity = modularBoatEntity.getCapability(TransportAPI.MODULAR_ENTITY);
        CompoundNBT moduleNBT = itemStack.getChildTag("modules");
        if (moduleNBT != null) {
            modularEntity.ifPresent(value -> value.deserializeNBT(moduleNBT));
        }
        return modularBoatEntity;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TranslationTextComponent("text.transport.hull_type", this.getHullType(stack.getTag())
                .getDisplayName()));
        tooltip.addAll(this.getModuleListToolTip(stack.getChildTag("modules")));
    }

    private HullType getHullType(CompoundNBT compoundNBT) {
        if (compoundNBT != null) {
            String hullTypeName = compoundNBT.getString("hull_type");
            if (!hullTypeName.isEmpty()) {
                HullType hullType = TransportAPI.HULL_TYPE.get().getValue(new ResourceLocation(hullTypeName));
                if (hullType != null) {
                    return hullType;
                }
            }
        }
        return TransportHullTypes.OAK.get();
    }

    @Override
    public EntityType<ModularBoatEntity> getEntityType() {
        return TransportEntities.MODULAR_BOAT.get();
    }
}
