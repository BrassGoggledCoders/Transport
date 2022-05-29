package xyz.brassgoggledcoders.transport.test;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraftforge.gametest.GameTestHolder;
import xyz.brassgoggledcoders.transport.Transport;

@GameTestHolder(Transport.ID)
@SuppressWarnings("unused")
public class TestDiamondCrossingRail {

    @GameTest
    public static void fromStraight(GameTestHelper helper) {
        helper.pulseRedstone(new BlockPos(5, 3, 3), 100);
        helper.succeedWhen(() -> helper.assertBlockProperty(new BlockPos(1, 2, 3), DetectorRailBlock.POWERED, true));
    }

    @GameTest(rotationSteps = 1, template = "fromstraight")
    public static void fromStraightCW90(GameTestHelper helper) {
        helper.pulseRedstone(new BlockPos(5, 3, 3), 100);
        helper.succeedWhen(() -> helper.assertBlockProperty(new BlockPos(1, 2, 3), DetectorRailBlock.POWERED, true));
    }

    @GameTest(rotationSteps = 2, template = "fromstraight")
    public static void fromStraightCW180(GameTestHelper helper) {
        helper.pulseRedstone(new BlockPos(5, 3, 3), 100);
        helper.succeedWhen(() -> helper.assertBlockProperty(new BlockPos(1, 2, 3), DetectorRailBlock.POWERED, true));
    }

    @GameTest(rotationSteps = 3, template = "fromstraight")
    public static void fromStraightCCW90(GameTestHelper helper) {
        helper.pulseRedstone(new BlockPos(5, 3, 3), 100);
        helper.succeedWhen(() -> helper.assertBlockProperty(new BlockPos(1, 2, 3), DetectorRailBlock.POWERED, true));
    }
}
