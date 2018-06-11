package xyz.brassgoggledcoders.transport.library.block.rail;

import com.teamacronymcoders.base.IBaseMod;
import com.teamacronymcoders.base.blocks.IAmBlock;
import com.teamacronymcoders.base.blocks.IHasItemBlock;
import com.teamacronymcoders.base.client.models.IHasModel;
import com.teamacronymcoders.base.items.itemblocks.ItemBlockModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BlockRailFoundation extends BlockRailBase implements IHasItemBlock, IHasModel, IAmBlock {
    private IBaseMod mod;
    private ItemBlock itemBlock;
    private String name;

    public BlockRailFoundation(String name) {
        super(false);
        this.itemBlock = new ItemBlockModel<>(this);
        this.name = name;
        this.setUnlocalizedName(name);
    }

    @Override
    public IBaseMod getMod() {
        return mod;
    }

    @Override
    public void setMod(IBaseMod mod) {
        this.mod = mod;
    }

    @Override
    public ItemBlock getItemBlock() {
        return itemBlock;
    }

    @Override
    public boolean canMakeSlopes(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFlexibleRail(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public List<String> getModelNames(List<String> modelNames) {
        modelNames.add(name);
        return modelNames;
    }

    @Override
    @Nonnull
    public abstract IProperty<EnumRailDirection> getShapeProperty();
}