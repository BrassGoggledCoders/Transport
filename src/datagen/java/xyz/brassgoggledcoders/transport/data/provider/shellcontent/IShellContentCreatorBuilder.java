package xyz.brassgoggledcoders.transport.data.provider.shellcontent;

import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;

public interface IShellContentCreatorBuilder {

    IShellContentCreator<?> build();
}
