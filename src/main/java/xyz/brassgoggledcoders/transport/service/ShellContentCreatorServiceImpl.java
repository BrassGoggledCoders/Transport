package xyz.brassgoggledcoders.transport.service;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
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
import java.util.Optional;

public class ShellContentCreatorServiceImpl extends SimpleJsonResourceReloadListener implements IShellContentCreatorService {
    public static final ShellContentCreatorInfo MISSING = new ShellContentCreatorInfo(
            Blocks.BEDROCK.defaultBlockState(),
            Blocks.BEDROCK.getName(),
            false,
            new EmptyShellContentCreator()
    );

    public static final Codec<Optional<ShellContentCreatorInfo>> CODEC = ConditionalOps.createConditionalCodec(ShellContentCreatorInfo.getCodec());

    private final BiMap<ResourceLocation, ShellContentCreatorInfo> creators;
    private int generation;

    public ShellContentCreatorServiceImpl() {
        super(new Gson(), "transport/shell_content");
        this.creators = HashBiMap.create();
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
            try {
                ICondition.getWithConditionalCodec(CODEC, JsonOps.INSTANCE, jsonObject)
                        .ifPresent(value -> newCreators.put(entry.getKey(), value));
            } catch (JsonParseException e) {
                Transport.LOGGER.warn("Failed to read file {}", fileName, e);
            }
        }

        Transport.LOGGER.info("Loaded {} Shell Content Creators", newCreators.size());
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
            ResourceLocation id = ResourceLocation.tryParse(nbt.getString(ShellContentCreatorInfo.NBT_TAG_ID));
            if (id != null) {
                return this.create(id, nbt.getCompound(ShellContentCreatorInfo.NBT_TAG_DATA));
            } else {
                return MISSING.create(nbt.getCompound(ShellContentCreatorInfo.NBT_TAG_DATA));
            }
        }
        return MISSING.create(null);
    }

    public void updateClient(Map<ResourceLocation, ShellContentCreatorInfo> infoList) {
        this.creators.clear();
        this.creators.putAll(infoList);
    }

    @Override
    @Nonnull
    public ShellContentCreatorInfo getEmpty() {
        return MISSING;
    }

    @Override
    public void writeData(@NotNull ShellContent shellContent, @NotNull CompoundTag parent) {
        CompoundTag shellContentNbt = new CompoundTag();

        ResourceLocation id = this.getId(shellContent.getCreatorInfo());

        shellContentNbt.putString(ShellContentCreatorInfo.NBT_TAG_ID, id.toString());
        shellContentNbt.put(ShellContentCreatorInfo.NBT_TAG_DATA, shellContent.serializeNBT());
        parent.put(ShellContentCreatorInfo.NBT_TAG_ELEMENT, shellContentNbt);
    }

    public ResourceLocation getId(ShellContentCreatorInfo creatorInfo) {
        return this.creators.inverse()
                .get(creatorInfo);
    }

    @Override
    public ShellContent readData(@NotNull CompoundTag parent) {
        if (parent.contains("shellContent")) {
            return this.create(parent.getCompound("shellContent"));
        } else if (parent.contains(ShellContentCreatorInfo.NBT_TAG_ELEMENT)) {
            return this.create(parent.getCompound(ShellContentCreatorInfo.NBT_TAG_ELEMENT));
        } else {
            return this.getEmpty().create(null);
        }
    }
}
