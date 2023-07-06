package xyz.brassgoggledcoders.transport.data.shellcontent;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import java.util.Collection;
import java.util.function.BiConsumer;

public class RegistrateShellContentDataProvider extends ShellContentDataProvider implements RegistrateProvider, BiConsumer<Collection<ICondition>, ShellContentCreatorInfo> {
    public static final ProviderType<RegistrateShellContentDataProvider> TYPE = ProviderType.register(
            "shell_content",
            (p, e) -> new RegistrateShellContentDataProvider(p, e.getGenerator())
    );

    private final AbstractRegistrate<?> owner;
    private BiConsumer<Collection<ICondition>, ShellContentCreatorInfo> consumer;

    public RegistrateShellContentDataProvider(AbstractRegistrate<?> owner, DataGenerator generator) {
        super(generator);
        this.owner = owner;
    }

    @Override
    @NotNull
    public LogicalSide getSide() {
        return LogicalSide.SERVER;
    }

    @Override
    protected void gather(BiConsumer<Collection<ICondition>, ShellContentCreatorInfo> consumer) {
        this.consumer = consumer;
        this.owner.genData(TYPE, this);
        this.consumer = null;
    }

    @Override
    public void accept(Collection<ICondition> iConditions, ShellContentCreatorInfo shellContentCreatorInfo) {
        if (this.consumer != null) {
            this.consumer.accept(iConditions, shellContentCreatorInfo);
        }
    }
}
