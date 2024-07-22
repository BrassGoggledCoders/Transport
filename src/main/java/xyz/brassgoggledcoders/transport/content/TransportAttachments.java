package xyz.brassgoggledcoders.transport.content;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.attachment.ShellContentItemAttachment;
import xyz.brassgoggledcoders.transport.capability.PatternedRailProvider;

public class TransportAttachments {
    public static final IRegisteringEntry<AttachmentType<PatternedRailProvider>, AttachmentType<?>> PATTERNED_RAIL_PROVIDER = Transport.getRegistering()
            .object("patterned_rail_provider")
            .simple(
                    NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
                    () -> AttachmentType.builder(PatternedRailProvider::new)
                            .serialize(PatternedRailProvider.CODEC)
                            .build()
            );

    public static final IRegisteringEntry<AttachmentType<ShellContentItemAttachment>, AttachmentType<?>> SHELL_CONTENT_ITEM = Transport.getRegistering()
            .object("shell_content_info")
            .simple(
                    NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
                    () -> AttachmentType.builder(() -> new ShellContentItemAttachment())
                            .serialize(ShellContentItemAttachment.CODEC)
                            .build()
            );

    public static void setup() {

    }
}
