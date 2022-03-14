package xyz.brassgoggledcoders.transport.json;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.content.TransportShellContentTypes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

public class ShellContentCreatorJsonManager extends SimpleJsonResourceReloadListener {
    private final Map<ResourceLocation, IShellContentCreator<?>> creators;

    public ShellContentCreatorJsonManager() {
        super(new Gson(), "transport/shell_content");
        this.creators = Maps.newHashMap();
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        Map<ResourceLocation, IShellContentCreator<?>> newCreators = Maps.newHashMap();
        pProfiler.push("Transport Shell Content Creators");

        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            String fileName = entry.getKey().toString();
            JsonObject jsonObject = GsonHelper.convertToJsonObject(entry.getValue(), fileName);
            ResourceLocation typeId = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "type"));
            if (typeId == null) {
                Transport.LOGGER.warn(fileName + " contained invalid 'type' value");
            } else {
                ShellContentType<?, ?> type = TransportShellContentTypes.SHELL_CONTENT_TYPES.get().getValue(typeId);
                if (type == null) {
                    Transport.LOGGER.warn(fileName + " contained a 'type' which does not exist");
                } else {
                    type.getCodec()
                            .decode(JsonOps.INSTANCE, jsonObject)
                            .resultOrPartial(error -> Transport.LOGGER.warn(fileName + " failed with error: " + error))
                            .ifPresent(pair -> newCreators.put(entry.getKey(), pair.getFirst()));
                }
            }
        }

        Transport.LOGGER.info("Loaded " + newCreators.size() + " Shell Content Creators");

        creators.clear();
        creators.putAll(newCreators);
        pProfiler.pop();
    }
}
