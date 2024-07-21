package xyz.brassgoggledcoders.transport.api.service;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public interface IShellContentCreatorService {
    int getGeneration();

    @Nullable
    ShellContentCreatorInfo getById(ResourceLocation id);

    @Nullable
    ResourceLocation getId(ShellContentCreatorInfo info);

    @Nonnull
    Collection<ShellContentCreatorInfo> getAll();

    @NotNull
    Map<ResourceLocation, ShellContentCreatorInfo> getMap();

    @Nonnull
    ShellContent create(ResourceLocation id, @Nullable CompoundTag nbt);

    @Nonnull
    ShellContent create(@Nullable CompoundTag nbt);

    @Nonnull
    ShellContentCreatorInfo getEmpty();

    void writeData(ShellContent shellContent, CompoundTag parent);

    ShellContent readData(CompoundTag parent);
}
