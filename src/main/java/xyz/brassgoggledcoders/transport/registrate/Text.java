package xyz.brassgoggledcoders.transport.registrate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.NonNullLazy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Text {
    private final String translationKey;
    private final NonNullLazy<ITextComponent> noChange;

    public Text(String translationKey) {
        this.translationKey = translationKey;
        this.noChange = NonNullLazy.of(() -> new TranslationTextComponent(translationKey));
    }

    @Nonnull
    public ITextComponent getTranslation(Object... args) {
        return new TranslationTextComponent(translationKey, args);
    }

    @Nonnull
    public ITextComponent getTranslation() {
        return noChange.get();
    }

    public void send(@Nullable PlayerEntity playerEntity) {
        this.send(playerEntity, false);
    }

    public void send(@Nullable PlayerEntity playerEntity, boolean actionBar) {
        if (playerEntity != null) {
            playerEntity.sendStatusMessage(this.getTranslation(), actionBar);
        }
    }

    public void send(@Nullable PlayerEntity playerEntity, Object... args) {
        this.send(playerEntity, false, args);
    }

    public void send(@Nullable PlayerEntity playerEntity, boolean actionBar, Object... args) {
        if (playerEntity != null) {
            playerEntity.sendStatusMessage(this.getTranslation(args), actionBar);
        }
    }
}
