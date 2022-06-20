package xyz.brassgoggledcoders.transport.api.service;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface IShellContentCreatorService {
    int getGeneration();

    @Nullable
    ShellContentCreatorInfo getById(ResourceLocation id);

    @Nonnull
    Collection<ShellContentCreatorInfo> getAll();

    @Nonnull
    ShellContent create(ResourceLocation id, @Nullable CompoundTag nbt);

    @Nonnull
    ShellContent create(@Nullable CompoundTag nbt);

    @Nonnull
    ShellContentCreatorInfo getEmpty();

    void writeData(ShellContent shellContent, CompoundTag parent);

    ShellContent readData(CompoundTag parent);
}
