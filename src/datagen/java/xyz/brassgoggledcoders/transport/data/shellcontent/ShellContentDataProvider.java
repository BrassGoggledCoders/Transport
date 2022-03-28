package xyz.brassgoggledcoders.transport.data.shellcontent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public abstract class ShellContentDataProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger(ShellContentDataProvider.class);
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private final DataGenerator generator;

    protected ShellContentDataProvider(DataGenerator generator) {
        this.generator = generator;
    }

    protected abstract void gather(Consumer<ShellContentCreatorInfo> consumer);

    @Override
    public void run(@Nonnull HashCache pCache) {
        List<ShellContentCreatorInfo> shellContentCreatorInfos = Lists.newArrayList();
        this.gather(shellContentCreatorInfos::add);

        Path path = this.generator.getOutputFolder();
        shellContentCreatorInfos.forEach(shellContentCreatorInfo -> {
            Path filePath = createPath(path, shellContentCreatorInfo.id());

            try {
                JsonElement jsonElement = ShellContentCreatorInfo.getCodec()
                        .encode(shellContentCreatorInfo, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())
                        .getOrThrow(false, LOGGER::warn);
                if (jsonElement.isJsonObject()) {
                    jsonElement.getAsJsonObject().remove("id");
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
