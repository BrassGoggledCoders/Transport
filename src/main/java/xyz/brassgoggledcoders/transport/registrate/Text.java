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
        if (args.length == 0) {
            return noChange.get();
        } else {
            return new TranslationTextComponent(translationKey, args);
        }
    }

    public void send(@Nullable PlayerEntity playerEntity, boolean actionBar, Object... args) {
        if (playerEntity != null) {
            playerEntity.sendStatusMessage(this.getTranslation(args), actionBar);
        }
    }
}
