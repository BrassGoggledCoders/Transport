package xyz.brassgoggledcoders.transport.screen.locomotive;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import xyz.brassgoggledcoders.transport.api.engine.EngineState;
import xyz.brassgoggledcoders.transport.network.property.Property;
import xyz.brassgoggledcoders.transport.network.property.PropertyManager;

import javax.annotation.Nonnull;
import java.util.EnumMap;

public class SpeedWidget extends Widget {
    private final Property<Integer> speed;
    private final PropertyManager propertyManager;
    private final int originX;
    private final int originY;
    private final EnumMap<EngineState, Double> stateAngles;

    private double angle;

    public SpeedWidget(int x, int y, int width, int height, Property<Integer> speed, PropertyManager propertyManager) {
        super(x, y, width, height, new StringTextComponent(""));
        this.originX = x + (width / 2);
        this.originY = y + height;
        this.speed = speed;
        this.propertyManager = propertyManager;
        this.stateAngles = Maps.newEnumMap(EngineState.class);
        for (EngineState engineState : EngineState.values()) {
            stateAngles.put(engineState, Math.toRadians(engineState.ordinal() * ((float) 180 / 6) - 90));
        }
        this.angle = stateAngles.get(EngineState.byId(speed.get()));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.push();
        matrixStack.translate(originX + 1, originY - 2, 0);
        matrixStack.rotate(Vector3f.ZN.rotation((float) angle));
        matrixStack.translate(-3.5D, -44, 0);
        this.blit(matrixStack, 0, 0, 230, 8, 7, 46);
        matrixStack.pop();
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        EngineState newEngineState = null;
        double shortedDistance = Math.PI / 3;
        for (EngineState engineState : EngineState.values()) {
            double distance = Math.abs(angle - stateAngles.get(engineState));
            if (distance < shortedDistance) {
                shortedDistance = distance;
                newEngineState = engineState;
            }
        }
        if (newEngineState != null) {
            propertyManager.updateServer(speed, newEngineState.ordinal());
        }
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.angle = Math.atan2(originY - mouseY, mouseX - originX) - (Math.PI / 2);
    }
}
