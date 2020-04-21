package xyz.brassgoggledcoders.transport.api.module.slot;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import xyz.brassgoggledcoders.transport.api.TransportObjects;

import java.util.Map;
import java.util.stream.Collectors;

public class ModuleSlots {


    public static final ModuleSlot NONE = new ModuleSlot("none", "module_slot.transport.none",
            ((modularEntity, module) -> false));

    public static final ModuleSlot CARGO = new ModuleSlot("cargo", "module_slot.transport.cargo",
            ((modularEntity, module) -> module.getType() == TransportObjects.CARGO_TYPE.get()));

    public static final ModuleSlot BACK = new ModuleSlot("back", "module_slot.transport.back",
            ((modularEntity, module) -> module.getType() != TransportObjects.CARGO_TYPE.get()));

    public static final Map<String, ModuleSlot> MODULE_SLOT_MAP = Lists.newArrayList(NONE, CARGO, BACK)
            .parallelStream()
            .collect(Collectors.toMap(ModuleSlot::getName, moduleSlot -> moduleSlot));
}
