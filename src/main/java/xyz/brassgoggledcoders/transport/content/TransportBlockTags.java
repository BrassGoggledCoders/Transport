package xyz.brassgoggledcoders.transport.content;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import xyz.brassgoggledcoders.transport.Transport;

public class TransportBlockTags {
    public static final ITag.INamedTag<Block> RAILS_REGULAR = BlockTags.createOptional(Transport.rl("rails/regular"));
    public static final ITag.INamedTag<Block> RAILS_POWERED = BlockTags.createOptional(Transport.rl("rails/powered"));
    public static final ITag.INamedTag<Block> RAILS_STRUCTURE = BlockTags.createOptional(Transport.rl("rails/structure"));

}
