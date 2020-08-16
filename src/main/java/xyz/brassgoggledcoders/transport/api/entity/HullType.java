package xyz.brassgoggledcoders.transport.api.entity;

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
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

public class HullType extends ForgeRegistryEntry<HullType> implements IItemProvider {
    private final NonNullSupplier<Item> itemSupplier;
    private final Lazy<ResourceLocation> entityTexture;
    private String translationKey;
    private ITextComponent displayName;

    public HullType(NonNullSupplier<Item> itemSupplier) {
        this.itemSupplier = itemSupplier;
        this.entityTexture = Lazy.of(this::getEntityTexture);
    }

    public HullType(RegistryObject<Item> registryObject, Lazy<ResourceLocation> entityTexture) {
        this(() -> registryObject.orElse(Items.AIR), entityTexture);
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
