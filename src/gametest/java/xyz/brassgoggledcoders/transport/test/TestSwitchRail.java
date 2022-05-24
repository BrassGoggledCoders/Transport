package xyz.brassgoggledcoders.transport.test;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraftforge.gametest.GameTestHolder;
import xyz.brassgoggledcoders.transport.Transport;

@GameTestHolder(Transport.ID)
public class TestSwitchRail {
    private static final BlockPos[] STRAIGHT_END_POS = new BlockPos[]{
            new BlockPos(3, 2, 1),
            new BlockPos(5, 2, 1)
    };

    private static final BlockPos[] DIVERGE_END_POS = new BlockPos[]{
            new BlockPos(1, 2, 3),
            new BlockPos(7, 2, 3)
    };

    private static final BlockPos[] POINT_END_POS = new BlockPos[]{
            new BlockPos(3, 2, 5),
            new BlockPos(5, 2, 5)
    };
    private static final BlockPos PULSE_POS = new BlockPos(4, 2, 5);

    private static final BlockPos PULSE_SWITCHES_POS = new BlockPos(4, 2, 3);

    @GameTest(template = "frompoint")
    public static void fromPointStraight(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : STRAIGHT_END_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

    @GameTest(rotationSteps = 1, template = "frompoint")
    public static void fromPointStraightCW90(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : STRAIGHT_END_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

    @GameTest(rotationSteps = 2, template = "frompoint")
    public static void fromPointStraightCW180(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : STRAIGHT_END_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

    @GameTest(rotationSteps = 3, template = "frompoint")
    public static void fromPointStraightCCW90(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : STRAIGHT_END_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

    @GameTest(template = "frompoint")
    public static void fromPointDiverge(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.pulseRedstone(PULSE_SWITCHES_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : DIVERGE_END_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

    @GameTest(rotationSteps = 1, template = "frompoint")
    public static void fromPointDivergeCW90(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.pulseRedstone(PULSE_SWITCHES_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : DIVERGE_END_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

    @GameTest(rotationSteps = 2, template = "frompoint")
    public static void fromPointDivergeCW180(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.pulseRedstone(PULSE_SWITCHES_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : DIVERGE_END_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

    @GameTest(rotationSteps = 3, template = "frompoint")
    public static void fromPointDivergeCCW90(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.pulseRedstone(PULSE_SWITCHES_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : DIVERGE_END_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

}
