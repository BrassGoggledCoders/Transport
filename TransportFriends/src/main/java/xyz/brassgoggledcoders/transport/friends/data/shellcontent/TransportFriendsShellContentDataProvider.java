package xyz.brassgoggledcoders.transport.friends.data.shellcontent;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.crafting.conditions.ICondition;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.data.shellcontent.ShellContentDataProvider;

import java.util.Collection;
import java.util.function.BiConsumer;

public class TransportFriendsShellContentDataProvider extends ShellContentDataProvider {
    public TransportFriendsShellContentDataProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void gather(BiConsumer<Collection<ICondition>, ShellContentCreatorInfo> consumer) {
        QuarkShellContent.gather(consumer);
    }
}
