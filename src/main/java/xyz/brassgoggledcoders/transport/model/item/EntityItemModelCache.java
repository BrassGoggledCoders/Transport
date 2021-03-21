package xyz.brassgoggledcoders.transport.model.item;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import xyz.brassgoggledcoders.transport.util.CachedValue;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class EntityItemModelCache implements IFutureReloadListener {
    public static final EntityItemModelCache INSTANCE = new EntityItemModelCache();

    private List<CachedValue<IBakedModel>> cachedValues = Lists.newArrayList();

    public void dirtyCaches() {
        cachedValues.forEach(CachedValue::invalidate);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public CompletableFuture<Void> reload(IStage stage, IResourceManager resourceManager, IProfiler preparationsProfiler, IProfiler reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.runAsync(this::dirtyCaches);
    }

    public static CachedValue<IBakedModel> getBakedModelCacheFor(Item item) {
        CachedValue<IBakedModel> cachedValue = new CachedValue<>(() ->
                Minecraft.getInstance()
                        .getItemRenderer()
                        .getItemModelMesher()
                        .getItemModel(item)
        );
        INSTANCE.cachedValues.add(cachedValue);
        return cachedValue;
    }


}
