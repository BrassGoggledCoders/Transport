package xyz.brassgoggledcoders.transport.compat.jei.module;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.item.IModularItem;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModuleCategory implements IRecipeCategory<ModuleRecipe> {
    public static final ResourceLocation UID = Transport.rl("module_configurator");

    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public ModuleCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("jei", "textures/gui/gui_vanilla.png");
        background = guiHelper.drawableBuilder(location, 0, 168, 125, 18)
                .addPadding(0, 20, 0, 0)
                .build();
        icon = guiHelper.createDrawableIngredient(new ItemStack(TransportBlocks.MODULE_CONFIGURATOR.get()));
        localizedName = I18n.format("screen.transport.jei.category.module_configurator");
    }

    @Override
    @Nonnull
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    @Nonnull
    public Class<? extends ModuleRecipe> getRecipeClass() {
        return ModuleRecipe.class;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return localizedName;
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    @Nonnull
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setIngredients(ModuleRecipe moduleRecipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Lists.newArrayList(moduleRecipe.getModuleItem(), moduleRecipe.getModularInput()));
        ingredients.setOutput(VanillaTypes.ITEM, moduleRecipe.getModularOutput());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setRecipe(IRecipeLayout recipeLayout, ModuleRecipe moduleRecipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(1, true, 49, 0);
        guiItemStacks.init(2, false, 107, 0);
        guiItemStacks.set(ingredients);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void draw(ModuleRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        Minecraft.getInstance().fontRenderer.func_243248_b(matrixStack, new TranslationTextComponent(
                        "text.transport.module_slot", recipe.getModuleSlot().getDisplayName()),
                0, 27, 4210752);
    }

    public static Collection<ModuleRecipe> createRecipes() {
        Collection<ModuleRecipe> moduleRecipes = Lists.newArrayList();
        List<Pair<ItemStack, IModularEntity>> modularEntities = ForgeRegistries.ITEMS
                .getValues()
                .parallelStream()
                .filter(item -> item instanceof IModularItem<?>)
                .map(item -> {
                    IModularItem<?> modularItem = (IModularItem<?>) item;
                    EntityType<?> entityType = modularItem.getEntityType();
                    Entity entity = entityType.create(Objects.requireNonNull(Minecraft.getInstance().world));
                    if (entity != null) {
                        Optional<IModularEntity> optional = entity.getCapability(TransportAPI.MODULAR_ENTITY).resolve();
                        if (optional.isPresent()) {
                            return Pair.of(new ItemStack(item), optional.get());
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        for (ModuleType moduleType : TransportAPI.MODULE_TYPE.get()) {
            for (Module<?> module : moduleType.getValues()) {
                ItemStack itemStack = new ItemStack(module.asItem());
                if (!itemStack.isEmpty()) {
                    for (Pair<ItemStack, IModularEntity> modularPair : modularEntities) {
                        IModularEntity modularEntity = modularPair.getRight();
                        if (modularEntity.canEquip(module) && module.isValidFor(modularEntity)) {
                            for (ModuleSlot moduleSlot : modularEntity.getModuleSlots()) {
                                if (moduleSlot.isModuleValid(modularEntity, module)) {
                                    modularEntity.add(module, moduleSlot, false);
                                    moduleRecipes.add(new ModuleRecipe(
                                            new ItemStack(module.asItem()),
                                            modularPair.getLeft(),
                                            modularEntity.asItemStack(),
                                            moduleSlot
                                    ));
                                    modularEntity.remove(moduleSlot, false);
                                }
                            }
                        }
                    }
                }
            }
        }
        return moduleRecipes;
    }
}
