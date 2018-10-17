package xyz.brassgoggledcoders.transport.hwyla;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.modulesystem.dependencies.IDependency;
import com.teamacronymcoders.base.modulesystem.dependencies.ModDependency;
import xyz.brassgoggledcoders.transport.Transport;

import java.util.List;

@Module(Transport.ID)
public class HwylaModule extends ModuleBase {
    @Override
    public String getName() {
        return "Hwyla";
    }

    @Override
    public List<IDependency> getDependencies(List<IDependency> dependencies) {
        dependencies.add(new ModDependency("waila"));
        return super.getDependencies(dependencies);
    }
}
