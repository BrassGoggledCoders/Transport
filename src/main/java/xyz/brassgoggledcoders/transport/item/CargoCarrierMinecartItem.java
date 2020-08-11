package xyz.brassgoggledcoders.transport.item;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.item.IModularItem;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportModuleSlots;
import xyz.brassgoggledcoders.transport.entity.CargoCarrierMinecartEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.minecraft.entity.item.minecart.AbstractMinecartEntity.Type.CHEST;

public class CargoCarrierMinecartItem extends MinecartItem implements IModularItem<CargoCarrierMinecartEntity> {
    public CargoCarrierMinecartItem() {
        this(new Item.Properties()
                .group(Transport.ITEM_GROUP));
    }

    public CargoCarrierMinecartItem(Properties properties) {
        super(CHEST, properties);
    }

    public static CargoModule getCargo(@Nullable CompoundNBT cargo) {
        return Optional.ofNullable(cargo)
                .map(compoundNBT -> compoundNBT.getString("name"))
                .map(TransportAPI::getCargo)
                .orElse(null);
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getPos();
        BlockState blockstate = world.getBlockState(blockPos);
        if (!blockstate.isIn(BlockTags.RAILS)) {
            return ActionResultType.FAIL;
        } else {
            ItemStack itemStack = context.getItem();
            if (!world.isRemote) {
                RailShape railshape = blockstate.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock) blockstate.getBlock()).getRailDirection(blockstate, world, blockPos, null) : RailShape.NORTH_SOUTH;
                double d0 = 0.0D;
                if (railshape.isAscending()) {
                    d0 = 0.5D;
                }

                CargoCarrierMinecartEntity cargoCarrierMinecartEntity = new CargoCarrierMinecartEntity(world,
                        blockPos.getX() + 0.5D,
                        blockPos.getY() + 0.0625D + d0, blockPos.getZ() + 0.5D);
                if (itemStack.hasDisplayName()) {
                    cargoCarrierMinecartEntity.setCustomName(itemStack.getDisplayName());
                }

                LazyOptional<IModularEntity> modularEntity = cargoCarrierMinecartEntity.getCapability(TransportAPI.MODULAR_ENTITY);
                CompoundNBT moduleNBT = itemStack.getChildTag("modules");
                if (moduleNBT != null) {
                    modularEntity.ifPresent(value -> value.deserializeNBT(moduleNBT));
                }

                CompoundNBT cargoNBT = itemStack.getChildTag("cargo");
                if (cargoNBT != null && cargoNBT.contains("name")) {
                    CargoModule cargoModule = TransportAPI.getCargo(cargoNBT.getString("name"));
                    if (cargoModule != null) {
                        modularEntity.ifPresent(value -> {
                            ModuleInstance<?> moduleInstance = value.add(cargoModule, TransportModuleSlots.CARGO.get(),
                                    false);
                            if (moduleInstance != null && cargoNBT.contains("instance")) {
                                moduleInstance.deserializeNBT(cargoNBT.getCompound("instance"));
                            }
                        });
                    }
                }

                if (context.getPlayer() != null) {
                    cargoCarrierMinecartEntity.setOriginalPushes(context.getPlayer());
                }

                world.addEntity(cargoCarrierMinecartEntity);
            }

            itemStack.shrink(1);
            return ActionResultType.SUCCESS;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.addAll(this.getModuleListToolTip(stack.getChildTag("modules")));
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        CargoModule cargoModule = getCargo(stack.getChildTag("cargo"));
        if (cargoModule != null) {
            return new TranslationTextComponent(
                    "text.transport.with",
                    Items.MINECART.getDisplayName(stack),
                    cargoModule.getDisplayName()
            );
        } else {
            return super.getDisplayName(stack);
        }
    }

    @Override
    public EntityType<CargoCarrierMinecartEntity> getEntityType() {
        return TransportEntities.CARGO_MINECART.get();
    }
}
