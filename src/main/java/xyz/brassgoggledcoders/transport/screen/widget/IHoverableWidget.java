package xyz.brassgoggledcoders.transport.screen.widget;

import net.minecraft.util.text.ITextProperties;

import javax.annotation.Nonnull;
import java.util.List;

public interface IHoverableWidget {
    @Nonnull
    List<? extends ITextProperties> getHoveredText();
}
