package xyz.brassgoggledcoders.transport.friends.data.shellcontent;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class ShellContentDataProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger(ShellContentDataProvider.class);
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private final DataGenerator generator;

    protected ShellContentDataProvider(DataGenerator generator) {
        this.generator = generator;
    }

    protected abstract void gather(BiConsumer<Collection<ICondition>, ShellContentCreatorInfo> consumer);

    @Override
    public void run(@Nonnull HashCache pCache) {
        List<Pair<Collection<ICondition>, ShellContentCreatorInfo>> shellContentCreatorInfos = Lists.newArrayList();
        this.gather((conditions, info) -> shellContentCreatorInfos.add(Pair.of(conditions, info)));

        Path path = this.generator.getOutputFolder();
        shellContentCreatorInfos.forEach(shellContentCreatorInfoPair -> {
            ShellContentCreatorInfo shellContentCreatorInfo = shellContentCreatorInfoPair.getRight();
            Path filePath = createPath(path, shellContentCreatorInfo.id());

            try {
                JsonElement jsonElement = ShellContentCreatorInfo.getCodec()
                        .encode(shellContentCreatorInfo, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())
                        .getOrThrow(false, LOGGER::warn);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    jsonObject.remove("id");
                    Collection<ICondition> conditions = shellContentCreatorInfoPair.getLeft();
                    if (!conditions.isEmpty()) {
                        JsonArray conditionsArray = new JsonArray();
                        for (ICondition condition : conditions) {
                            conditionsArray.add(CraftingHelper.serialize(condition));
                        }
                        jsonObject.add("conditions", conditionsArray);
                    }
                }
                DataProvider.save(GSON, pCache, jsonElement, filePath);
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't save shell content creator {}", filePath, ioexception);
            }
        });
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
