package xyz.brassgoggledcoders.transport.routing.instruction;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class TimeRouting extends Routing {
    private static final Predicate<String> TIME_PATTERNS = Pattern.compile("\\d{1,2}:\\d{1,2}").asPredicate();
    private static final Map<String, Integer> TIMES = createTimeMap();

    private final int startTime;
    private final int endTime;

    public TimeRouting(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean matches(@Nonnull AbstractMinecartEntity minecartEntity) {
        long dayTime = minecartEntity.getEntityWorld().getDayTime() % 24000L;
        return dayTime > startTime && dayTime < endTime;
    }

    @Nonnull
    public static Either<String, Routing> create(List<String> inputs) {
        int startTime = -1;
        int endTime = 24000;
        if (inputs.size() >= 1) {
            Either<String, Integer> parsedStart = parseTimeString(inputs.get(0));
            if (parsedStart.left().isPresent()) {
                return parsedStart.mapBoth(error -> error, value -> null);
            } else if (parsedStart.right().isPresent()){
                startTime = parsedStart.right().get();
            }
        }
        if (inputs.size() > 1) {
            Either<String, Integer> parsedEnd = parseTimeString(inputs.get(1));
            if (parsedEnd.left().isPresent()) {
                return parsedEnd.mapBoth(error -> error, value -> null);
            } else if (parsedEnd.right().isPresent()){
                endTime = parsedEnd.right().get();
            }
        }

        if (startTime >= 0 && (endTime > 0 && endTime > startTime)) {
            return Either.right(new TimeRouting(startTime, endTime));
        } else {
            return Either.left("Found invalid times: " + startTime + " & " + endTime);
        }
    }

    public static Either<String, Integer> parseTimeString(String timeString) {
        timeString = timeString.trim();
        Integer time = TIMES.get(timeString.toLowerCase(Locale.ENGLISH));
        if (time == null) {
            if (TIME_PATTERNS.test(timeString)) {
                String[] times = timeString.split(":");
                Either<String, Integer> hour = parseGroup(times[0]);
                Either<String, Integer> minute = parseGroup(times[1]);
                if (hour.left().isPresent()) {
                    return hour;
                } else if (minute.left().isPresent()) {
                    return minute;
                } else {
                    int totalHour = (hour.right().map(value -> value % 24).orElse(0) * 1000) - 6000;
                    if (totalHour < 0) {
                        totalHour += 24000;
                    }
                    int totalMinute = Math.round(minute.right().map(value -> value % 60).orElse(0) * 16.6F);
                    return Either.right(totalHour + totalMinute);
                }
            }
        } else {
            return Either.right(time);
        }

        return Either.left("Couldn't convert " + timeString + " to time");
    }

    private static Either<String, Integer> parseGroup(String group) {
        try {
            return Either.right(Integer.parseInt(group));
        } catch (ClassCastException exception) {
            return Either.left(group + " is not a number");
        }

    }

    private static Map<String, Integer> createTimeMap() {
        Map<String, Integer> times = Maps.newHashMap();
        times.put("day", 1000);
        times.put("noon", 6000);
        times.put("night", 13000);
        times.put("midnight", 18000);
        return times;
    }
}
