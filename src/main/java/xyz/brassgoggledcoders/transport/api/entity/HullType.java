package xyz.brassgoggledcoders.transport.api.entity;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public class HullType extends ForgeRegistryEntry<HullType> implements IItemProvider {
    private final NonNullSupplier<Item> itemSupplier;
    private final Lazy<ResourceLocation> entityTexture;
    private String translationKey;
    private ITextComponent displayName;

    public HullType(Supplier<Item> itemSupplier) {
        this.itemSupplier = () -> {
            if (itemSupplier.get() == null) {
                return itemSupplier.get();
            } else {
                return Items.AIR;
            }
        };
        this.entityTexture = Lazy.of(this::getEntityTexture);
    }

    public HullType(RegistryObject<Item> registryObject, Lazy<ResourceLocation> entityTexture) {
        this(() -> registryObject.orElse(Items.AIR), entityTexture);
    }

    public HullType(NonNullSupplier<Item> itemSupplier, ResourceLocation entityTexture) {
        this(itemSupplier, Lazy.of(() -> {
            if (entityTexture != null) {
                return new ResourceLocation(entityTexture.getNamespace(), "textures/entity/" + entityTexture.getPath());
            } else {
                return null;
            }
        }));
    }

    public HullType(NonNullSupplier<Item> itemSupplier, Lazy<ResourceLocation> entityTexture) {
        this.itemSupplier = itemSupplier;
        this.entityTexture = entityTexture;
    }

    @Nullable
    public ResourceLocation getEntityTexture(@Nonnull Entity entity) {
        return entityTexture.get();
    }

    @Nullable
    private ResourceLocation getEntityTexture() {
        ResourceLocation registryName = this.getRegistryName();
        if (registryName != null) {
            return new ResourceLocation(registryName.getNamespace(), "textures/entity/hull/" +
                    registryName.getPath() + ".png");
        } else {
            return null;
        }
    }

    @Nonnull
    public ITextComponent getDisplayName() {
        if (this.displayName == null) {
            this.displayName = new TranslationTextComponent(this.getTranslationKey());
        }
        return this.displayName;
    }

    @Nonnull
    public String getTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("hull_type", this.getRegistryName());
        }
        return this.translationKey;
    }

    public Collection<RenderMaterial> getRenderMaterials() {
        return Collections.singleton(new RenderMaterial(PlayerContainer.LOCATION_BLOCKS_TEXTURE,
                this.entityTexture.get()));
    }

    @Override
    @Nonnull
    public Item asItem() {
        return itemSupplier.get();
    }
}
