package xyz.brassgoggledcoders.transport.data.provider.shellcontent;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.apache.commons.compress.utils.Lists;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class ShellContentInfoBuilder {
    private final List<ICondition> conditions = Lists.newArrayList();
    private BlockState viewState;
    private Component name;
    private boolean createRecipe = true;
    private IShellContentCreatorBuilder shellContentCreatorBuilder;

    public ShellContentInfoBuilder withViewState(BlockState viewState) {
        this.viewState = viewState;
        return this;
    }

    public ShellContentInfoBuilder withName(Component name) {
        this.name = name;
        return this;
    }

    public ShellContentInfoBuilder withCreateRecipe(boolean createRecipe) {
        this.createRecipe = createRecipe;
        return this;
    }

    public ShellContentInfoBuilder withShellContentCreator(IShellContentCreatorBuilder shellContentCreator) {
        this.shellContentCreatorBuilder = shellContentCreator;
        return this;
    }

    public ShellContentInfoBuilder withConditions(ICondition... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public void build(BiConsumer<ResourceLocation, WithConditions<ShellContentCreatorInfo>> infoConsumer) {
        if (this.viewState == null) {
            throw new IllegalStateException("viewState is required");
        }
        build(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(this.viewState.getBlock())), infoConsumer);
    }

    public void build(@Nonnull ResourceLocation id, BiConsumer<ResourceLocation, WithConditions<ShellContentCreatorInfo>> infoConsumer) {
        validate();
        infoConsumer.accept(
                id,
                new WithConditions<>(
                        new ShellContentCreatorInfo(
                                this.viewState,
                                this.name != null ? this.name : this.viewState.getBlock().getName(),
                                this.createRecipe,
                                this.shellContentCreatorBuilder.build()
                        ),
                        this.conditions.toArray(ICondition[]::new)
                )
        );
    }

    protected void validate() {
        if (this.viewState == null) {
            throw new IllegalStateException("viewState is required");
        }
        if (this.shellContentCreatorBuilder == null) {
            throw new IllegalStateException("shellContentCreatorBuilder is required");
        }
    }

    public static ShellContentInfoBuilder of(Block viewState) {
        return of(viewState.defaultBlockState());
    }

    public static ShellContentInfoBuilder of(BlockState viewState) {
        return new ShellContentInfoBuilder()
                .withViewState(viewState);
    }
}
