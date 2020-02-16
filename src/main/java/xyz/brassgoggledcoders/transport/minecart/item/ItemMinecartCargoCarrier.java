package xyz.brassgoggledcoders.transport.minecart.item;

import com.teamacronymcoders.base.entities.EntityMinecartBase;
import com.teamacronymcoders.base.items.minecart.ItemMinecartBase;
import com.teamacronymcoders.base.util.CapUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.CapabilityProviderCargoCarrierItem;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;
import xyz.brassgoggledcoders.transport.minecart.entity.EntityMinecartCargoCarrier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemMinecartCargoCarrier extends ItemMinecartBase {
    public ItemMinecartCargoCarrier() {
        super("cargo_carrier");
        this.setHasSubtypes(false);
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public EntityMinecartBase getEntityFromItem(World world, ItemStack itemStack) {
        return new EntityMinecartCargoCarrier(world, CapUtils.getOptional(itemStack, CapabilityCargo.CARRIER)
                .map(ICargoCarrier::getCargo)
                .orElseGet(TransportAPI.getCargoRegistry()::getEmpty));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CapabilityProviderCargoCarrierItem(stack);
    }

    @Override
    public List<ItemStack> getAllSubItems(List<ItemStack> itemStacks) {
        for (Cargo cargo : TransportAPI.getCargoRegistry().getEntries()) {
            ItemStack itemStack = new ItemStack(this);
            NBTTagCompound cargoNBT = new NBTTagCompound();
            cargoNBT.setString("name", cargo.getRegistryName().toString());
            itemStack.setTagInfo("cargo", cargoNBT);
            itemStacks.add(itemStack);
        }
        return itemStacks;
    }

    @Override
    public List<ResourceLocation> getResourceLocations(List<ResourceLocation> resourceLocations) {
        resourceLocations.add(new ResourceLocation(Transport.ID, "tesr"));
        return resourceLocations;
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(@Nonnull ItemStack cartItemStack) {
        String displayName = "";

        displayName += Items.MINECART.getItemStackDisplayName(cartItemStack);

        displayName += CapUtils.getOptional(cartItemStack, CapabilityCargo.CARRIER)
                .map(ICargoCarrier::getCargoInstance)
                .map(ICargoInstance::getLocalizedName)
                .map(name -> " " + I18n.format("transport.separator.with") + " " + name)
                .orElse("");

        return displayName;
    }
}
