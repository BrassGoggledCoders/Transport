package xyz.brassgoggledcoders.transport.routing.instruction;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.api.routing.instruction.RoutingInstruction;

import javax.annotation.Nonnull;

public class NameTagRoutingInstruction extends RoutingInstruction {
    private final String name;

    public NameTagRoutingInstruction(String name) {
        this.name = name;
    }

    @Override
    public boolean matches(@Nonnull AbstractMinecartEntity minecartEntity) {
        ITextComponent minecartName = minecartEntity.getCustomName();
        return minecartName != null && name.equals(minecartName.getString());
    }
}
