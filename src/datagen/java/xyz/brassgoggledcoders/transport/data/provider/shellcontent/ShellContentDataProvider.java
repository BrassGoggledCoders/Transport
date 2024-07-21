package xyz.brassgoggledcoders.transport.data.provider.shellcontent;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class ShellContentDataProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger(ShellContentDataProvider.class);
    private static final Codec<Optional<WithConditions<ShellContentCreatorInfo>>> CODEC = ExtraCodecs.lazyInitializedCodec(
            () -> ConditionalOps.createConditionalCodecWithConditions(ShellContentCreatorInfo.getCodec())
    );
    private final DataGenerator generator;

    protected ShellContentDataProvider(DataGenerator generator) {
        this.generator = generator;
    }

    protected abstract void gather(BiConsumer<ResourceLocation, WithConditions<ShellContentCreatorInfo>> consumer);

    @Override
    @NotNull
    public CompletableFuture<?> run(@Nonnull CachedOutput pCache) {
        List<Pair<ResourceLocation, WithConditions<ShellContentCreatorInfo>>> shellContentCreatorInfos = Lists.newArrayList();
        this.gather((conditions, info) -> shellContentCreatorInfos.add(Pair.of(conditions, info)));

        PackOutput path = this.generator.getPackOutput();
        return CompletableFuture.allOf(shellContentCreatorInfos.stream()
                .map(shellContentCreatorInfoPair -> {
                    WithConditions<ShellContentCreatorInfo> shellContentCreatorInfo = shellContentCreatorInfoPair.getRight();
                    Path filePath = createPath(path.getOutputFolder(), shellContentCreatorInfoPair.getKey());

                    JsonElement jsonElement = CODEC.encode(Optional.of(shellContentCreatorInfo), JsonOps.INSTANCE, JsonOps.INSTANCE.empty())
                            .getOrThrow(false, LOGGER::warn);
                    return DataProvider.saveStable(pCache, jsonElement, filePath);
                })
                .toArray(CompletableFuture[]::new)
        );
    }

    @Override
    @Nonnull
    public String getName() {
        return "Shell Content Creator Data";
    }

    private static Path createPath(Path pPath, ResourceLocation pId) {
        return pPath.resolve("data/" + pId.getNamespace() + "/transport/shell_content/" + pId.getPath() + ".json");
    }
}
