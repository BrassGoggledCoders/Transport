package xyz.brassgoggledcoders.transport.test;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraftforge.gametest.GameTestHolder;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.Transport;

@GameTestHolder(Transport.ID)
public class TestWyeSwitchRail {
    private static final BlockPos[] STRAIGHT_END_POS = new BlockPos[]{
            new BlockPos(3, 2, 1),
            new BlockPos(5, 2, 5)
    };

    private static final BlockPos[] NARROW_END_POS = new BlockPos[]{
            new BlockPos(1, 2, 3),
            new BlockPos(7, 2, 3)
    };

    private static final BlockPos[] DIVERGE_END_POS = new BlockPos[]{
            new BlockPos(3, 2, 5),
            new BlockPos(5, 2, 1)
    };

    private static final BlockPos PULSE_SWITCHES_POS = new BlockPos(4, 2, 3);

    @GameTest(template = "fromnarrow")
    public static void fromNarrowStraight(GameTestHelper helper) {
        runTest(helper, NARROW_END_POS, STRAIGHT_END_POS, null);
    }

    @GameTest(rotationSteps = 1, template = "fromnarrow")
    public static void fromNarrowStraightCW90(GameTestHelper helper) {
        runTest(helper, NARROW_END_POS, STRAIGHT_END_POS, null);
    }

    @GameTest(rotationSteps = 2, template = "fromnarrow")
    public static void fromNarrowStraightCW180(GameTestHelper helper) {
        runTest(helper, NARROW_END_POS, STRAIGHT_END_POS, null);
    }

    @GameTest(rotationSteps = 3, template = "fromnarrow")
    public static void fromNarrowStraightCCW90(GameTestHelper helper) {
        runTest(helper, NARROW_END_POS, STRAIGHT_END_POS, null);
    }

    @GameTest(template = "fromnarrow")
    public static void fromNarrowDiverge(GameTestHelper helper) {
        runTest(helper, NARROW_END_POS, DIVERGE_END_POS, PULSE_SWITCHES_POS);
    }

    @GameTest(rotationSteps = 1, template = "fromnarrow")
    public static void fromNarrowDivergeCW90(GameTestHelper helper) {
        runTest(helper, NARROW_END_POS, DIVERGE_END_POS, PULSE_SWITCHES_POS);
    }

    @GameTest(rotationSteps = 2, template = "fromnarrow")
    public static void fromNarrowDivergeCW180(GameTestHelper helper) {
        runTest(helper, NARROW_END_POS, DIVERGE_END_POS, PULSE_SWITCHES_POS);
    }

    @GameTest(rotationSteps = 3, template = "fromnarrow")
    public static void fromNarrowDivergeCCW90(GameTestHelper helper) {
        runTest(helper, NARROW_END_POS, DIVERGE_END_POS, PULSE_SWITCHES_POS);
    }

    @GameTest(template = "fromstraight")
    public static void fromStraight(GameTestHelper helper) {
        runTest(helper, STRAIGHT_END_POS, NARROW_END_POS, null);
    }

    @GameTest(rotationSteps = 1, template = "fromstraight")
    public static void fromStraightCW90(GameTestHelper helper) {
        runTest(helper, STRAIGHT_END_POS, NARROW_END_POS, null);
    }

    @GameTest(rotationSteps = 2, template = "fromstraight")
    public static void fromStraightCW180(GameTestHelper helper) {
        runTest(helper, STRAIGHT_END_POS, NARROW_END_POS, null);
    }

    @GameTest(rotationSteps = 3, template = "fromstraight")
    public static void fromStraightCCW90(GameTestHelper helper) {
        runTest(helper, STRAIGHT_END_POS, NARROW_END_POS, null);
    }

    @GameTest(template = "fromstraight")
    public static void fromStraightDiverge(GameTestHelper helper) {
        runTest(helper, STRAIGHT_END_POS, NARROW_END_POS, null);
    }

    @GameTest(rotationSteps = 1, template = "fromstraight")
    public static void fromStraightDivergeCW90(GameTestHelper helper) {
        runTest(helper, STRAIGHT_END_POS, NARROW_END_POS, null);
    }

    @GameTest(rotationSteps = 2, template = "fromstraight")
    public static void fromStraightDivergeCW180(GameTestHelper helper) {
        runTest(helper, STRAIGHT_END_POS, NARROW_END_POS, null);
    }

    @GameTest(rotationSteps = 3, template = "fromstraight")
    public static void fromStraightDivergeCCW90(GameTestHelper helper) {
        runTest(helper, STRAIGHT_END_POS, NARROW_END_POS, null);
    }

    @GameTest(template = "fromdiverge")
    public static void fromDivergeStraight(GameTestHelper helper) {
        runTest(helper, DIVERGE_END_POS, NARROW_END_POS, null);
    }

    @GameTest(rotationSteps = 1, template = "fromdiverge")
    public static void fromDivergeStraightCW90(GameTestHelper helper) {
        runTest(helper, DIVERGE_END_POS, NARROW_END_POS, null);
    }

    @GameTest(rotationSteps = 2, template = "fromdiverge")
    public static void fromDivergeStraightCW180(GameTestHelper helper) {
        runTest(helper, DIVERGE_END_POS, NARROW_END_POS, null);
    }

    @GameTest(rotationSteps = 3, template = "fromdiverge")
    public static void fromDivergeStraightCCW90(GameTestHelper helper) {
        runTest(helper, DIVERGE_END_POS, NARROW_END_POS, null);
    }

    @GameTest(template = "fromdiverge")
    public static void fromDivergeDiverge(GameTestHelper helper) {
        runTest(helper, DIVERGE_END_POS, NARROW_END_POS, PULSE_SWITCHES_POS);
    }

    @GameTest(rotationSteps = 1, template = "fromdiverge")
    public static void fromDivergeDivergeCW90(GameTestHelper helper) {
        runTest(helper, DIVERGE_END_POS, NARROW_END_POS, PULSE_SWITCHES_POS);
    }

    @GameTest(rotationSteps = 2, template = "fromdiverge")
    public static void fromDivergeDivergeCW180(GameTestHelper helper) {
        runTest(helper, DIVERGE_END_POS, NARROW_END_POS, PULSE_SWITCHES_POS);
    }

    @GameTest(rotationSteps = 3, template = "fromdiverge")
    public static void fromDivergeDivergeCCW90(GameTestHelper helper) {
        runTest(helper, DIVERGE_END_POS, NARROW_END_POS, PULSE_SWITCHES_POS);
    }

    private static void runTest(GameTestHelper helper, BlockPos[] startPos, BlockPos[] endPos, @Nullable BlockPos switchPulsePos) {
        for (BlockPos blockPos : startPos) {
            helper.pulseRedstone(blockPos.above(), 100);
        }
        if (switchPulsePos != null) {
            helper.pulseRedstone(switchPulsePos, 100);
        }
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : endPos) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

}
