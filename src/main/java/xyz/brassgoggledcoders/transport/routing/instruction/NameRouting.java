package xyz.brassgoggledcoders.transport.routing.instruction;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;
import java.util.List;

public class NameRouting extends Routing {
    private final List<String> names;

    public NameRouting(List<String> names) {
        this.names = names;
    }

    @Override
    public boolean matches(@Nonnull AbstractMinecartEntity minecartEntity) {
        ITextComponent minecartName = minecartEntity.getCustomName();
        return minecartName != null && names.contains(minecartName.getString());
    }
}
