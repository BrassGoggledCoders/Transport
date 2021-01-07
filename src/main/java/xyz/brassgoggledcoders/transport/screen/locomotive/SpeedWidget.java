package xyz.brassgoggledcoders.transport.screen.locomotive;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import xyz.brassgoggledcoders.transport.api.engine.EngineState;
import xyz.brassgoggledcoders.transport.network.property.Property;
import xyz.brassgoggledcoders.transport.network.property.PropertyManager;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.EnumMap;

public class SpeedWidget extends Widget {
    private final SteamLocomotiveScreen screen;
    private final Property<Integer> speed;
    private final PropertyManager propertyManager;
    private final int originX;
    private final int originY;
    private final EnumMap<EngineState, Double> stateAngles;

    private double angle;

    public SpeedWidget(SteamLocomotiveScreen screen, int x, int y, int width, int height) {
        super(x, y, width, height, new StringTextComponent(""));
        this.screen = screen;
        this.originX = x + (width / 2);
        this.originY = y + height;
        this.speed = screen.getContainer().getSpeed();
        this.propertyManager = screen.getContainer().getPropertyManager();
        this.stateAngles = Maps.newEnumMap(EngineState.class);
        for (EngineState engineState : EngineState.values()) {
            stateAngles.put(engineState, Math.toRadians(engineState.ordinal() * ((float) 180 / 6) - 90));
        }
        this.angle = stateAngles.get(EngineState.byId(speed.get()));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        Integer speed = this.speed.get();
        if (!screen.isDragging()) {
            EngineState engineState = this.getEngineState();
            if (engineState == null) {
                if (speed != null) {
                    this.angle = this.stateAngles.get(EngineState.byId(speed));
                }
            } else if (speed != null && engineState.ordinal() != speed) {
                this.angle = this.stateAngles.get(EngineState.byId(speed));
            }
        }
        matrixStack.push();
        matrixStack.translate(originX + 1, originY - 2, 0);
        matrixStack.rotate(Vector3f.ZN.rotation((float) angle));
        matrixStack.translate(-3.5D, -44, 0);
        this.blit(matrixStack, 0, 0, 230, 8, 7, 46);
        matrixStack.pop();
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        EngineState newEngineState = this.getEngineState();
        Integer currentSpeed = this.speed.get();
        if (newEngineState != null) {
            this.angle = stateAngles.get(newEngineState);
            propertyManager.updateServer(speed, newEngineState.ordinal());
        } else if (currentSpeed != null) {
            this.angle = stateAngles.get(EngineState.byId(currentSpeed));
        }
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.angle = Math.atan2(originY - mouseY, mouseX - originX) - (Math.PI / 2);
    }

    @Override
    public void renderToolTip(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
        EngineState engineState = this.getEngineState();
        if (engineState != null) {
            GuiUtils.drawHoveringText(matrixStack, Collections.singletonList(
                    new TranslationTextComponent("screen.transport.speed", engineState.getDisplayName())),
                    mouseX, mouseY, screen.width, screen.height, -1, Minecraft.getInstance().fontRenderer);
        }
    }

    public EngineState getEngineState() {
        EngineState newEngineState = null;
        double shortedDistance = Math.PI / 3;
        for (EngineState engineState : EngineState.values()) {
            double distance = Math.abs(angle - stateAngles.get(engineState));
            if (distance < shortedDistance) {
                shortedDistance = distance;
                newEngineState = engineState;
            }
        }
        return newEngineState;
    }
}
