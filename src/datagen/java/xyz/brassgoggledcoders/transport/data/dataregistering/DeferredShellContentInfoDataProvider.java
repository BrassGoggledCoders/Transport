package xyz.brassgoggledcoders.transport.data.dataregistering;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.WithConditions;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.DeferredProviderType;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.data.provider.shellcontent.ShellContentDataProvider;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DeferredShellContentInfoDataProvider extends ShellContentDataProvider implements BiConsumer<ResourceLocation, WithConditions<ShellContentCreatorInfo>> {
    private final Supplier<List<Consumer<DeferredShellContentInfoDataProvider>>> deferredActions;

    private BiConsumer<ResourceLocation, WithConditions<ShellContentCreatorInfo>> heldConsumer;

    public DeferredShellContentInfoDataProvider(DataGenerator dataGenerator, Supplier<List<Consumer<DeferredShellContentInfoDataProvider>>> deferredActions) {
        super(dataGenerator);

        this.deferredActions = deferredActions;
    }

    @Override
    protected void gather(BiConsumer<ResourceLocation, WithConditions<ShellContentCreatorInfo>> consumer) {
        this.heldConsumer = consumer;
        this.deferredActions.get()
                .forEach(deferredAction -> deferredAction.accept(this));
        this.heldConsumer = null;
    }

    @Override
    public void accept(ResourceLocation resourceLocation, WithConditions<ShellContentCreatorInfo> shellContentCreatorInfoWithConditions) {
        Objects.requireNonNull(this.heldConsumer)
                .accept(resourceLocation, shellContentCreatorInfoWithConditions);
    }
}
