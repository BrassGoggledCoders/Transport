package xyz.brassgoggledcoders.transport.api.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.chat.Component;

public class ComponentCodec implements Codec<Component> {
    @Override
    public <T> DataResult<Pair<Component, T>> decode(DynamicOps<T> ops, T input) {
        return DataResult.success(Pair.of(
                Component.Serializer.fromJson(ops.convertTo(JsonOps.INSTANCE, input)),
                input
        ));
    }

    @Override
    public <T> DataResult<T> encode(Component input, DynamicOps<T> ops, T prefix) {
        return DataResult.success(JsonOps.INSTANCE.convertTo(ops, Component.Serializer.toJsonTree(input)));
    }
}
