package xyz.brassgoggledcoders.transport.tracks;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import xyz.brassgoggledcoders.transport.Transport;

@Module(Transport.ID)
public class TracksModule extends ModuleBase {
    @Override
    public String getName() {
        return "Tracks";
    }
}
