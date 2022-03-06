package xyz.brassgoggledcoders.transport.test;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.minecraftforge.gametest.GameTestHolder;
import xyz.brassgoggledcoders.transport.Transport;

@GameTestHolder(Transport.ID)
public class TestDumpRail {
    @GameTest
    public static void itemFlat(GameTestHelper helper) {
        BlockPos barrelPos = new BlockPos(1, 1, 6);
        helper.pulseRedstone(new BlockPos(2, 2, 2), 100);
        helper.succeedWhen(() -> helper.assertContainerContains(barrelPos, Items.RAIL));
    }

    @GameTest
    public static void itemFlatTall(GameTestHelper helper) {
        BlockPos barrelPos = new BlockPos(1, 1, 6);
        helper.pulseRedstone(new BlockPos(2, 2, 2), 100);
        helper.succeedWhen(() -> helper.assertContainerContains(barrelPos, Items.RAIL));
    }

    @GameTest
    public static void itemAscending(GameTestHelper helper) {
        BlockPos barrelPos = new BlockPos(1, 1, 6);
        helper.pulseRedstone(new BlockPos(2, 4, 2), 100);
        helper.succeedWhen(() -> helper.assertContainerContains(barrelPos, Items.RAIL));
    }

    @GameTest
    public static void itemAscendingTall(GameTestHelper helper) {
        BlockPos barrelPos = new BlockPos(1, 1, 6);
        helper.pulseRedstone(new BlockPos(2, 4, 2), 100);
        helper.succeedWhen(() -> helper.assertContainerContains(barrelPos, Items.RAIL));
    }
}
