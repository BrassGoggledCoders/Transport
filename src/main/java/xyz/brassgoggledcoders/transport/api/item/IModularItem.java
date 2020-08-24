package xyz.brassgoggledcoders.transport.api.item;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;

import java.util.Collection;
import java.util.List;

public interface IModularItem<T extends Entity> {
    EntityType<T> getEntityType();

    default Collection<ITextComponent> getModuleListToolTip(CompoundNBT modulesNBT) {
        List<ITextComponent> toolTips = Lists.newArrayList();
        if (modulesNBT != null) {
            ListNBT moduleInstancesNBT = modulesNBT.getList("moduleInstances", Constants.NBT.TAG_COMPOUND);
            if (moduleInstancesNBT.size() > 0) {
                toolTips.add(new TranslationTextComponent("text.transport.installed_modules"));
                for (int x = 0; x < moduleInstancesNBT.size(); x++) {
                    CompoundNBT moduleInstanceNBT = moduleInstancesNBT.getCompound(x);
                    Module<?> module = Module.fromCompoundNBT(moduleInstanceNBT);
                    ModuleSlot moduleSlot = TransportAPI.getModuleSlot(moduleInstanceNBT.getString("moduleSlot"));
                    if (module != null && moduleSlot != null) {
                        toolTips.add(new TranslationTextComponent("text.transport.installed_module",
                                moduleSlot.getDisplayName(), module.getDisplayName()));
                    }
                }
            }
        }
        return toolTips;
    }
}
