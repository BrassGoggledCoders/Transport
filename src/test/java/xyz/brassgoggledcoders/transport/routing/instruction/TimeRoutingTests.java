package xyz.brassgoggledcoders.transport.routing.instruction;

import com.mojang.datafixers.util.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TimeRoutingTests {
    @Test
    @DisplayName("DAY should return 1000")
    void testNamedTime() {
        Either<String, Integer> time = TimeRouting.parseTimeString("DAY");
        Assertions.assertTrue(time.right().isPresent());
        Assertions.assertEquals(time.right().get(), 1000);
    }

    @Test
    @DisplayName("6:00 should return 0")
    void testClockTime() {
        Either<String, Integer> time = TimeRouting.parseTimeString("6:00");
        Assertions.assertTrue(time.right().isPresent());
        Assertions.assertEquals(time.right().get(), 0);
    }
}
