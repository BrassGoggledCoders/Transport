package xyz.brassgoggledcoders.transport.content;

import net.neoforged.neoforge.attachment.AttachmentType;
import xyz.brassgoggledcoders.transport.capability.PatternedRailProvider;

public class TransportAttachments {
    public static final AttachmentType<PatternedRailProvider> PATTERNED_RAIL_PROVIDER = AttachmentType.builder(PatternedRailProvider::new)
            .serialize(PatternedRailProvider.CODEC)
            .build();
}
