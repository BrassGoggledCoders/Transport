package xyz.brassgoggledcoders.transport.test;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraftforge.gametest.GameTestHolder;
import xyz.brassgoggledcoders.transport.Transport;

@GameTestHolder(Transport.ID)
public class TestOneWayBoosterRail {

    @GameTest
    public static void fromZero(GameTestHelper helper) {
        BlockPos detectorPos = new BlockPos(6, 2, 1);
        helper.pulseRedstone(new BlockPos(1, 2, 2), 100);
        helper.succeedWhen(() -> helper.assertBlockProperty(detectorPos, DetectorRailBlock.POWERED, true));
    }

    @GameTest(rotationSteps = 1, template = "fromzero")
    public static void fromZeroCW90(GameTestHelper helper) {
        BlockPos detectorPos = new BlockPos(6, 2, 1);
        helper.pulseRedstone(new BlockPos(1, 2, 2), 100);
        helper.succeedWhen(() -> helper.assertBlockProperty(detectorPos, DetectorRailBlock.POWERED, true));
    }

    @GameTest(rotationSteps = 2, template = "fromzero")
    public static void fromZeroCW180(GameTestHelper helper) {
        BlockPos detectorPos = new BlockPos(6, 2, 1);
        helper.pulseRedstone(new BlockPos(1, 2, 2), 100);
        helper.succeedWhen(() -> helper.assertBlockProperty(detectorPos, DetectorRailBlock.POWERED, true));
    }

    @GameTest(rotationSteps = 3, template = "fromzero")
    public static void fromZeroCCW90(GameTestHelper helper) {
        BlockPos detectorPos = new BlockPos(6, 2, 1);
        helper.pulseRedstone(new BlockPos(1, 2, 2), 100);
        helper.succeedWhen(() -> helper.assertBlockProperty(detectorPos, DetectorRailBlock.POWERED, true));
    }
}
