package xyz.brassgoggledcoders.transport.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.Transport;

import java.util.Optional;

public record ShellContentItemAttachment(
        @NotNull ResourceLocation id,
        @NotNull Optional<CompoundTag> savedTag
) {
    public static final Codec<ShellContentItemAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(ShellContentItemAttachment::id),
            CompoundTag.CODEC.optionalFieldOf("savedTag").forGetter(ShellContentItemAttachment::savedTag)
    ).apply(instance, instance.stable(ShellContentItemAttachment::new)));

    public ShellContentItemAttachment() {
        this(Transport.rl("missing"));
    }

    public ShellContentItemAttachment(ResourceLocation id) {
        this(id, Optional.empty());
    }
}
