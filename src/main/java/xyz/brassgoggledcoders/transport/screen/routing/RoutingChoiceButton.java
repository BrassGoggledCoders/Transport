package xyz.brassgoggledcoders.transport.screen.routing;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.container.routing.RoutingToolContainer;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingEdge;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNode;

import javax.annotation.Nonnull;
import java.util.function.IntSupplier;

public class RoutingChoiceButton extends Button {
    private final RoutingToolContainer container;
    private final IntSupplier offsetSupplier;
    private final int index;

    public RoutingChoiceButton(RoutingToolContainer container, IntSupplier offsetSupplier, int index, int x, int y) {
        super(x, y, 89, 20, StringTextComponent.EMPTY, button -> Transport.LOGGER.warn("CLICKED: " + index));
        this.container = container;
        this.offsetSupplier = offsetSupplier;
        this.index = index;
        this.visible = false;
    }

    @Override
    public void renderToolTip(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
        if (this.isHovered && container.getNeighbors().size() > this.index + offsetSupplier.getAsInt()) {
            Pair<RoutingNode, RoutingEdge> neighbor = container.getNeighbors().get(this.index + offsetSupplier.getAsInt());
        }
    }

    public int getIndex() {
        return index;
    }
}
