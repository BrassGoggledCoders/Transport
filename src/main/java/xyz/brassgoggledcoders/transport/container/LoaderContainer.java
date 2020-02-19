package xyz.brassgoggledcoders.transport.container;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.tileentity.loader.BasicLoaderTileEntity;

public class LoaderContainer extends BasicInventoryContainer {
    private final BasicLoaderTileEntity<?, ?> basicLoaderTileEntity;

    public LoaderContainer(int id, PlayerInventory inventory, BasicLoaderTileEntity<?, ?> basicLoaderTileEntity) {
        super(TransportBlocks.LOADER_CONTAINER.get(), inventory, id, IAssetProvider.DEFAULT_PROVIDER);
        this.initInventory();
        basicLoaderTileEntity.getIntResourceHolders()
                .forEach(this::trackInt);
        basicLoaderTileEntity.getSlots()
                .forEach(this::addSlot);
        this.basicLoaderTileEntity = basicLoaderTileEntity;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        BlockPos pos = basicLoaderTileEntity.getPos();
        return player.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D,
                (double)pos.getZ() + 0.5D) <= 64.0D;
    }

    public BasicLoaderTileEntity<?, ?> getLoader() {
        return basicLoaderTileEntity;
    }

    public static LoaderContainer create(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        TileEntity tileEntity = inventory.player.world.getTileEntity(packetBuffer.readBlockPos());
        if (tileEntity instanceof BasicLoaderTileEntity) {
            return new LoaderContainer(id, inventory, (BasicLoaderTileEntity<?, ?>) tileEntity);
        }
        throw new IllegalStateException("Failed to Find Loader Tile Entity");
    }
}
