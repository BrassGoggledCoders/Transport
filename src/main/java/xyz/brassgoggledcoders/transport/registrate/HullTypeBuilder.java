package xyz.brassgoggledcoders.transport.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import xyz.brassgoggledcoders.transport.api.entity.HullType;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class HullTypeBuilder<T extends HullType, P> extends AbstractBuilder<HullType, T, P, HullTypeBuilder<T, P>> {
    private final Supplier<T> hullTypeCreator;

    public static <T extends HullType, P> HullTypeBuilder<T, P> create(AbstractRegistrate<?> owner, P parent,
                                                                       String name, BuilderCallback builderCallback,
                                                                       Supplier<T> hullCreator) {
        return new HullTypeBuilder<>(owner, parent, name, builderCallback, hullCreator);
    }

    private HullTypeBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                              Supplier<T> hullTypeCreator) {
        super(owner, parent, name, callback, HullType.class);
        this.hullTypeCreator = hullTypeCreator;
    }

    public HullTypeBuilder<T, P> lang(String name) {
        return this.lang(HullType::getTranslationKey, name);
    }

    @Override
    @Nonnull
    protected T createEntry() {
        return hullTypeCreator.get();
    }
}
