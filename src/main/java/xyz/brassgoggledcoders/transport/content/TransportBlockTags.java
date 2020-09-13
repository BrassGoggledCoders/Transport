package xyz.brassgoggledcoders.transport.content;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

public class TransportBlockTags {
    public static final ITag.INamedTag<Block> REGULAR_RAILS = BlockTags.makeWrapperTag("forge:rails/regular");
    public static final ITag.INamedTag<Block> POWERED_RAILS = BlockTags.makeWrapperTag("forge:rails/powered");
    public static final ITag.INamedTag<Block> STRUCTURE_RAILS = BlockTags.makeWrapperTag("forge:rails/structure");

}
