package xyz.brassgoggledcoders.transport.capability.supervisor;

import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

public enum NonnullDirection implements INameable {
    DOWN(Direction.DOWN),
    UP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST),
    NONE(null);

    private final Direction direction;
    private ITextComponent name;

    NonnullDirection(@Nullable Direction direction) {
        this.direction = direction;
    }

    @Nullable
    public Direction getDirection() {
        return direction;
    }

    @Nonnull
    public static NonnullDirection fromDirection(@Nullable Direction direction) {
        if (direction != null) {
            for (NonnullDirection nonnullDirection : values()) {
                if (nonnullDirection.getDirection() == direction) {
                    return nonnullDirection;
                }
            }
        }
        return NONE;
    }

    public String getTranslationKey() {
        return "text.transport.direction." + this.toString().toLowerCase(Locale.US);
    }

    @Override
    @Nonnull
    public ITextComponent getName() {
        if (this.name == null) {
            this.name = new TranslationTextComponent(this.getTranslationKey());
        }
        return this.name;
    }
}
