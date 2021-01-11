package xyz.brassgoggledcoders.transport.navigation;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.transport.api.navigation.INavigationNetwork;
import xyz.brassgoggledcoders.transport.api.navigation.INavigationPoint;
import xyz.brassgoggledcoders.transport.api.navigation.NavigationPointType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class NavigationNetwork implements INavigationNetwork {
    private final Map<UUID, INavigationPoint> navigationPoints;
    private final Multimap<UUID, INavigationPoint> knownNavigationPoints;

    public NavigationNetwork() {
        this.navigationPoints = Maps.newHashMap();
        this.knownNavigationPoints = Multimaps.newSetMultimap(Maps.newHashMap(), Sets::newHashSet);
    }

    @Nonnull
    @Override
    public Collection<INavigationPoint> getNavigationPoints() {
        return navigationPoints.values();
    }

    @Override
    public boolean addNavigationPoint(@Nonnull INavigationPoint navigationPoint) {
        if (!navigationPoints.containsKey(navigationPoint.getUniqueId())) {
            navigationPoints.put(navigationPoint.getUniqueId(), navigationPoint);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public INavigationPoint removeNavigationPoint(@Nonnull UUID uniqueId) {
        return navigationPoints.remove(uniqueId);
    }

    @Nullable
    @Override
    public INavigationPoint getNavigationPoint(@Nonnull UUID uniqueId) {
        return navigationPoints.get(uniqueId);
    }

    @Nonnull
    @Override
    public Collection<INavigationPoint> getKnownNavigationPoints(@Nonnull PlayerEntity playerEntity) {
        return knownNavigationPoints.get(playerEntity.getUniqueID());
    }

    @Override
    public boolean setKnownNavigationPoint(@Nonnull PlayerEntity playerEntity, @Nonnull INavigationPoint navigationPoint, boolean known) {
        if (known) {
            return knownNavigationPoints.get(playerEntity.getUniqueID()).add(navigationPoint);
        } else {
            return knownNavigationPoints.get(playerEntity.getUniqueID()).remove(navigationPoint);
        }
    }

    @Override
    @Nonnull
    public INavigationPoint createPoint(@Nonnull NavigationPointType pointType) {
        return pointType.create(this);
    }

    @Override
    @Nonnull
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        ListNBT navigationPointListNBT = new ListNBT();
        for (INavigationPoint navigationPoint : this.navigationPoints.values()) {
            navigationPointListNBT.add(NavigationPointType.serialize(navigationPoint));
        }
        compoundNBT.put("navigationPoints", navigationPointListNBT);
        CompoundNBT knownNavigationPointsNBT = new CompoundNBT();
        for (Map.Entry<UUID, Collection<INavigationPoint>> entry : this.knownNavigationPoints.asMap().entrySet()) {
            ListNBT playerKnownNavigationPointListNBT = new ListNBT();
            for (INavigationPoint navigationPoint : entry.getValue()) {
                playerKnownNavigationPointListNBT.add(NBTUtil.func_240626_a_(navigationPoint.getUniqueId()));
            }
            knownNavigationPointsNBT.put(entry.getKey().toString(), playerKnownNavigationPointListNBT);
        }
        compoundNBT.put("knownNavigationPoints", knownNavigationPointsNBT);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.navigationPoints.clear();
        this.knownNavigationPoints.clear();
        ListNBT navigationPointsListNBT = nbt.getList("navigationPoints", Constants.NBT.TAG_COMPOUND);
        for (int x = 0; x < navigationPointsListNBT.size(); x++) {
            INavigationPoint navigationPoint = NavigationPointType.deserialize(this,
                    navigationPointsListNBT.getCompound(x));
            if (navigationPoint != null) {
                this.navigationPoints.put(navigationPoint.getUniqueId(), navigationPoint);
            }
        }
        CompoundNBT knownNavigationPointsNBT = nbt.getCompound("knownNavigationPoints");
        for (String key : knownNavigationPointsNBT.keySet()) {
            UUID uniqueId = UUID.fromString(key);
            ListNBT playerKnownNavigationPointsListNBT = knownNavigationPointsNBT.getList(key, Constants.NBT.TAG_INT_ARRAY);
            for (INBT playerKnownNavigationPoint : playerKnownNavigationPointsListNBT) {
                UUID navigationPointUniqueId = NBTUtil.readUniqueId(playerKnownNavigationPoint);
                INavigationPoint navigationPoint = this.navigationPoints.get(navigationPointUniqueId);
                if (navigationPoint != null) {
                    this.knownNavigationPoints.get(uniqueId).add(navigationPoint);
                }
            }
        }
    }
}
