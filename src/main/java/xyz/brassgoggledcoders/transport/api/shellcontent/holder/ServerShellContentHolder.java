package xyz.brassgoggledcoders.transport.api.shellcontent.holder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.service.IShellContentCreatorService;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ServerShellContentHolder implements IShellContentHolder {
    private final IShellContentCreatorService manager;
    private final IShell shell;
    private ShellContent shellContent;
    private int generation;
    private Component name;

    public ServerShellContentHolder(IShellContentCreatorService manager, IShell shell) {
        this.manager = manager;
        this.shell = shell;
    }

    private void checkGeneration() {
        if (this.generation != manager.getGeneration()) {
            CompoundTag nbt = new CompoundTag();
            if (this.shellContent != null) {
                this.manager.writeData(shellContent, nbt);
                this.shellContent.invalidateCaps();
            }

            this.shellContent = this.manager.create(nbt);
            this.shellContent.setShell(this.shell);
            this.name = this.shell.getWithName(this.shellContent);

            this.generation = this.manager.getGeneration();
            this.shell.newGeneration();
        }
    }

    @Override
    @Nonnull
    public ShellContent get() {
        checkGeneration();

        return this.shellContent;
    }

    @Override
    public void update(ShellContent shellContent) {
        this.generation = this.manager.getGeneration();
        this.shellContent = shellContent;
        this.shellContent.setShell(this.shell);
        this.name = this.shell.getWithName(this.shellContent);
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf byteBuf) {
        Optional<CompoundTag> tag = this.get().getCreatorInfo().asTag();
        byteBuf.writeBoolean(tag.isPresent());
        tag.ifPresent(byteBuf::writeNbt);
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf byteBuf) {

    }

    @Override
    @Nonnull
    public Component getName() {
        checkGeneration();

        return this.name;
    }

    @Override
    public ItemStack asItemStack() {
        ItemStack itemStack = this.shell.asItemStack();
        TransportAPI.SHELL_CONTENT_CREATOR.get()
                .writeData(this.shellContent, itemStack.getOrCreateTag());
        return itemStack;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Id", this.shellContent.getCreatorInfo().id().toString());
        tag.put("Data", this.shellContent.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.update(this.manager.create(
                new ResourceLocation(nbt.getString("Id")),
                nbt.getCompound("Data")
        ));
    }
}
