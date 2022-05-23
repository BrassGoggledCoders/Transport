package xyz.brassgoggledcoders.transport.test;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraftforge.gametest.GameTestHolder;
import xyz.brassgoggledcoders.transport.Transport;

@GameTestHolder(Transport.ID)
public class TestSwitchRail {
    private static final BlockPos[] DETECTOR_POS = new BlockPos[]{
            new BlockPos(3, 2, 1),
            new BlockPos(5, 2, 1)
    };

    private static final BlockPos PULSE_POS = new BlockPos(4, 2, 5);

    @GameTest
    public static void valid(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : DETECTOR_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

    @GameTest(rotationSteps = 1, template = "valid")
    public static void validCW90(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : DETECTOR_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

    @GameTest(rotationSteps = 2, template = "valid")
    public static void validCW180(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : DETECTOR_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }

    @GameTest(rotationSteps = 3, template = "valid")
    public static void validCCW90(GameTestHelper helper) {
        helper.pulseRedstone(PULSE_POS, 100);
        helper.succeedWhen(() -> {
            for (BlockPos blockPos : DETECTOR_POS) {
                helper.assertBlockProperty(blockPos, DetectorRailBlock.POWERED, true);
            }
        });
    }
}
