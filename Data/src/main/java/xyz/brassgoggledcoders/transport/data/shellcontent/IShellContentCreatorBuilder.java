package xyz.brassgoggledcoders.transport.data.shellcontent;

import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;

public interface IShellContentCreatorBuilder {

    IShellContentCreator<?> build();
}
