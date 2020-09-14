package xyz.brassgoggledcoders.transport.content;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

public class TransportBlockTags {
    public static final ITag.INamedTag<Block> RAILS_REGULAR = BlockTags.makeWrapperTag("transport:rails/regular");
    public static final ITag.INamedTag<Block> RAILS_POWERED = BlockTags.makeWrapperTag("transport:rails/powered");
    public static final ITag.INamedTag<Block> RAILS_STRUCTURE = BlockTags.makeWrapperTag("transport:rails/structure");

}
