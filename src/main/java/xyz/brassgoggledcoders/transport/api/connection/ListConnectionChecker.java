package xyz.brassgoggledcoders.transport.api.connection;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ListConnectionChecker implements IConnectionChecker {
    private final List<IConnectionChecker> connectionCheckerList;

    public ListConnectionChecker() {
        this.connectionCheckerList = Lists.newArrayList();
    }

    @Override
    public boolean areConnected(@Nonnull Entity one, @Nonnull Entity two) {
        for (IConnectionChecker connectionChecker : connectionCheckerList) {
            if (connectionChecker.areConnected(one, two)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public Entity getLeader(@Nullable Entity entity) {
        for (IConnectionChecker connectionChecker : connectionCheckerList) {
            Entity leader = connectionChecker.getLeader(entity);
            if (leader != null) {
                return leader;
            }
        }
        return null;
    }

    public List<IConnectionChecker> getConnectionCheckers() {
        return connectionCheckerList;
    }
}
