package xyz.brassgoggledcoders.transport.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import xyz.brassgoggledcoders.transport.network.property.Property;
import xyz.brassgoggledcoders.transport.network.property.PropertyManager;

import javax.annotation.Nonnull;

public class PowerButton extends AbstractButton {
    private final Property<Boolean> property;
    private final PropertyManager propertyManager;

    public PowerButton(int x, int y, int width, int height, Property<Boolean> property, PropertyManager propertyManager) {
        super(x, y, width, height, new StringTextComponent(""));
        this.property = property;
        this.propertyManager = propertyManager;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public void onPress() {
        Boolean currentValue = property.get();
        if (currentValue != null) {
            this.propertyManager.updateServer(property, !currentValue);
        }
    }
}
