package xyz.brassgoggledcoders.transport.util;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableNetwork;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.functional.Trial;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class NBTHelper {
    public static <U, V> CompoundTag writeNetwork(
            MutableNetwork<U, V> network,
            Function<U, UUID> getUUID,
            Function<U, CompoundTag> nodeToTag,
            Function<V, CompoundTag> edgeToTag
    ) {
        CompoundTag networkTag = new CompoundTag();

        ListTag nodeListTag = new ListTag();
        network.nodes()
                .stream()
                .map(nodeToTag)
                .forEach(nodeListTag::add);
        networkTag.put("Nodes", nodeListTag);

        ListTag edgeListTag = new ListTag();
        for (V edge : network.edges()) {
            CompoundTag edgeTag = edgeToTag.apply(edge);
            EndpointPair<U> edgeNodes = network.incidentNodes(edge);
            edgeTag.putUUID("U", getUUID.apply(edgeNodes.nodeU()));
            edgeTag.putUUID("V", getUUID.apply(edgeNodes.nodeV()));
            edgeListTag.add(edgeTag);
        }
        networkTag.put("Edges", edgeListTag);

        return networkTag;
    }

    public static <U, V> void readNetwork(
            CompoundTag networkTag,
            Consumer<U> nodeConsumer,
            BiConsumer<V, Pair<UUID, UUID>> edgeConsumer,
            Function<CompoundTag, Trial<U>> tagToNode,
            Function<CompoundTag, V> tagToEdge
    ) {
        ListTag nodeListTag = networkTag.getList("Nodes", Tag.TAG_COMPOUND);
        for (int i = 0; i < nodeListTag.size(); i++) {
            tagToNode.apply(nodeListTag.getCompound(i))
                    .match(
                            nodeConsumer,
                            Transport.LOGGER::warn
                    );
        }

        ListTag edgeListTag = networkTag.getList("Edges", Tag.TAG_COMPOUND);
        for (int i = 0; i < edgeListTag.size(); i++) {
            CompoundTag edgeTag = edgeListTag.getCompound(i);
            edgeConsumer.accept(
                    tagToEdge.apply(edgeTag),
                    Pair.of(
                            edgeTag.getUUID("U"),
                            edgeTag.getUUID("V")
                    )
            );
        }
    }
}
