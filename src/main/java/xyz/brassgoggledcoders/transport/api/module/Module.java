package xyz.brassgoggledcoders.transport.api.module;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class Module<MOD extends Module<MOD>> extends ForgeRegistryEntry<MOD> implements IItemProvider {
    private final BiFunction<MOD, IModularEntity, ? extends ModuleInstance<MOD>> instanceCreator;
    private final Supplier<ModuleType> componentType;
    private String translationKey;
    private ITextComponent name;

    public Module(Supplier<ModuleType> componentType, BiFunction<MOD, IModularEntity,
            ? extends ModuleInstance<MOD>> instanceCreator) {
        this.componentType = componentType;
        this.instanceCreator = instanceCreator;
    }

    @Nonnull
    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.makeTranslationKey(componentType.get().getName(), this.getRegistryName());
        }
        return translationKey;
    }

    @Nonnull
    public ITextComponent getDisplayName() {
        if (name == null) {
            name = new TranslationTextComponent(this.getTranslationKey());
        }
        return name;
    }

    public boolean isValidFor(IModularEntity modularEntity) {
        return true;
    }

    public ModuleType getType() {
        return componentType.get();
    }

    public boolean isActive() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public ModuleInstance<MOD> createInstance(IModularEntity carrier) {
        return instanceCreator.apply((MOD) this, carrier);
    }

    @Nullable
    public static Module<?> fromPacketBuffer(PacketBuffer packetBuffer) {
        ModuleType moduleType = TransportAPI.getModuleType(packetBuffer.readResourceLocation());
        if (moduleType != null) {
            return moduleType.load(packetBuffer.readResourceLocation());
        } else {
            return null;
        }
    }

    public static void toPacketBuffer(Module<?> module, PacketBuffer packetBuffer) {
        packetBuffer.writeResourceLocation(Objects.requireNonNull(module.getType().getRegistryName()));
        packetBuffer.writeResourceLocation(Objects.requireNonNull(module.getRegistryName()));
    }

    public static Module<?> fromCompoundNBT(CompoundNBT compoundNBT) {
        ModuleType moduleType = TransportAPI.getModuleType(compoundNBT.getString("type"));
        if (moduleType != null) {
            return moduleType.load(compoundNBT.getString("module"));
        } else {
            return null;
        }
    }

    public static void toCompoundNBT(Module<?> module, CompoundNBT compoundNBT) {
        compoundNBT.putString("type", String.valueOf(module.getType().getRegistryName()));
        compoundNBT.putString("module", String.valueOf(module.getRegistryName()));
    }
}
