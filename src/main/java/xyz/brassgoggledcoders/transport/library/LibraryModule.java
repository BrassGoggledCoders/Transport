package xyz.brassgoggledcoders.transport.library;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import xyz.brassgoggledcoders.transport.Transport;

@Module(Transport.ID)
public class LibraryModule extends ModuleBase {
    @Override
    public String getName() {
        return "Library";
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }
}
