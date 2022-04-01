package xyz.brassgoggledcoders.transport.service;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.service.IShellContentCreatorService;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.shellcontent.empty.EmptyShellContentCreator;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Map;

public class ShellContentCreatorServiceImpl extends SimpleJsonResourceReloadListener implements IShellContentCreatorService {
    public static final ShellContentCreatorInfo MISSING = new ShellContentCreatorInfo(
            Transport.rl("missing"),
            Blocks.BEDROCK.defaultBlockState(),
            Blocks.BEDROCK.getName(),
            false,
            new EmptyShellContentCreator()
    );

    private final Map<ResourceLocation, ShellContentCreatorInfo> creators;
    private int generation;

    public ShellContentCreatorServiceImpl() {
        super(new Gson(), "transport/shell_content");
        this.creators = Maps.newHashMap();
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        Map<ResourceLocation, ShellContentCreatorInfo> newCreators = Maps.newHashMap();
        pProfiler.push("Transport Shell Content Creators");

        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            String fileName = entry.getKey().toString();
            JsonObject jsonObject = GsonHelper.convertToJsonObject(entry.getValue(), fileName);
            jsonObject.addProperty("id", fileName);
            if (CraftingHelper.processConditions(jsonObject, "conditions")) {
                ShellContentCreatorInfo.getCodec()
                        .decode(JsonOps.INSTANCE, jsonObject)
                        .resultOrPartial(error -> Transport.LOGGER.warn(fileName + " failed with error: " + error))
                        .ifPresent(pair -> newCreators.put(entry.getKey(), pair.getFirst()));
            }

        }

        Transport.LOGGER.info("Loaded " + newCreators.size() + " Shell Content Creators");
        this.generation++;
        creators.clear();
        creators.putAll(newCreators);
        pProfiler.pop();
    }

    @Override
    public int getGeneration() {
        return this.generation;
    }

    @Override
    @Nullable
    public ShellContentCreatorInfo getById(ResourceLocation id) {
        return this.creators.get(id);
    }

    @Override
    @Nonnull
    public Collection<ShellContentCreatorInfo> getAll() {
        return this.creators.values();
    }

    @Override
    @Nonnull
    public ShellContent create(ResourceLocation id, @Nullable CompoundTag nbt) {
        ShellContentCreatorInfo info = this.getById(id);

        if (info == null) {
            info = MISSING;
        }

        return info.create(nbt);
    }

    @NotNull
    @Override
    public ShellContent create(@Nullable CompoundTag nbt) {
        if (nbt != null) {
            ResourceLocation id = ResourceLocation.tryParse(nbt.getString("id"));
            if (id != null) {
                return this.create(id, nbt.getCompound("data"));
            } else {
                return MISSING.create(nbt.getCompound("data"));
            }
        }
        return MISSING.create(null);
    }

    @Override
    @Nonnull
    public ShellContentCreatorInfo getEmpty() {
        return MISSING;
    }
}
