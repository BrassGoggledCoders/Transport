package xyz.brassgoggledcoders.transport.screen;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import xyz.brassgoggledcoders.transport.api.module.ModuleTab;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ModularScreenInfo {
    private static final ModularScreenInfo EMPTY = new ModularScreenInfo(
            (short) -1,
            null,
            -1,
            UUID.randomUUID(),
            Collections.emptyList()
    );

    private static ModularScreenInfo current = null;

    private final short windowId;
    private final ContainerType<?> containerType;
    private final int entityId;
    private final UUID picked;
    private final List<ModuleTab> moduleTabList;

    public ModularScreenInfo(short windowId, ContainerType<?> containerType, int entityId, UUID picked, List<ModuleTab> moduleTabList) {
        this.windowId = windowId;
        this.containerType = containerType;
        this.entityId = entityId;
        this.picked = picked;
        this.moduleTabList = moduleTabList;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public UUID getPicked() {
        return picked;
    }

    public List<ModuleTab> getModuleTabList() {
        return moduleTabList;
    }

    public boolean matches(Container container) {
        return container.windowId == windowId && container.getType() == containerType;
    }

    public static void setCurrent(ModularScreenInfo modularScreenInfo) {
        current = modularScreenInfo;
    }

    public static ModularScreenInfo getCurrent() {
        if (current == null) {
            return EMPTY;
        } else {
            return current;
        }
    }
}
