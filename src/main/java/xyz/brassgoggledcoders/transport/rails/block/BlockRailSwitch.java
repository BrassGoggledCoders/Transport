package xyz.brassgoggledcoders.transport.rails.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import xyz.brassgoggledcoders.transport.library.block.rail.BlockRailFoundation;
import xyz.brassgoggledcoders.transport.library.block.rail.RailPredicates;

import javax.annotation.Nonnull;

public class BlockRailSwitch extends BlockRailFoundation {
    public static final PropertyEnum<EnumRailDirection> SWITCH_SHAPE = PropertyEnum.create("switch_shape",
            EnumRailDirection.class, RailPredicates.FLAT_STRAIGHT::test);
    public static final PropertyBool NORTH_WEST = PropertyBool.create("north_west");
    public static final PropertyBool LEFT = PropertyBool.create("left");

    public BlockRailSwitch() {
        super("switch");
    }

    @Nonnull
    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return SWITCH_SHAPE;
    }
}
