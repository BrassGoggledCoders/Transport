package xyz.brassgoggledcoders.transport.api.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class HullType extends ForgeRegistryEntry<HullType> implements IItemProvider {
    private final static ResourceLocation OAK_BOAT_DEFAULT = new ResourceLocation("textures/entity/boat/oak.png");
    private final Supplier<Item> itemSupplier;
    private ResourceLocation entityTexture;

    public HullType(Supplier<Item> itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    public HullType(Supplier<Item> itemSupplier, ResourceLocation entityTexture) {
        this(itemSupplier);
        this.entityTexture = entityTexture;
    }

    public ResourceLocation getEntityTexture(Entity entity) {
        if (entityTexture == null) {
            ResourceLocation registryName = this.getRegistryName();
            if (registryName != null) {
                entityTexture = new ResourceLocation(registryName.getNamespace(), "textures/entity/hull/" +
                        registryName.getPath() + ".png");
            }
        }
        return entityTexture;
    }

    @Override
    @Nonnull
    public Item asItem() {
        return itemSupplier.get();
    }
}
