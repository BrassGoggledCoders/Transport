package xyz.brassgoggledcoders.transport.predicate;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParser;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParserException;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class TimePredicate implements Predicate<Entity> {
    private static final Predicate<String> TIME_PATTERNS = Pattern.compile("\\d{1,2}:\\d{1,2}").asPredicate();
    private static final Map<String, Integer> TIMES = createTimeMap();

    private final int startTime;
    private final int endTime;

    public TimePredicate(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean test(Entity entity) {
        long dayTime = entity.getEntityWorld().getDayTime() % 24000L;
        return dayTime > startTime && dayTime < endTime;
    }

    @Nonnull
    public static TimePredicate create(PredicateParser predicateParser) throws PredicateParserException {
        int startTime = -1;
        int endTime = 24000;
        if (predicateParser.hasNextString()) {
            startTime = parseTimeString(predicateParser.getNextString());
        }
        if (predicateParser.hasNextString()) {
            endTime = parseTimeString(predicateParser.getNextString());
        }

        if (startTime >= 0 && (endTime > 0 && endTime > startTime)) {
            return new TimePredicate(startTime, endTime);
        } else {
            throw new PredicateParserException("Found invalid times: " + startTime + " & " + endTime);
        }
    }

    public static int parseTimeString(String timeString) throws PredicateParserException {
        timeString = timeString.trim();
        Integer time = TIMES.get(timeString.toLowerCase(Locale.ENGLISH));
        if (time == null) {
            if (TIME_PATTERNS.test(timeString)) {
                String[] times = timeString.split(":");
                int hour = parseGroup(times[0]);
                int minute = parseGroup(times[1]);
                int totalHour = (hour % 24 * 1000) - 6000;
                if (totalHour < 0) {
                    totalHour += 24000;
                }
                int totalMinute = Math.round(minute % 60 * 16.6F);
                return totalHour + totalMinute;
            }
        } else {
            return time;
        }

        throw new PredicateParserException("Couldn't convert " + timeString + " to time");
    }

    private static int parseGroup(String group) throws PredicateParserException {
        try {
            return Integer.parseInt(group);
        } catch (NumberFormatException exception) {
            throw new PredicateParserException(group + " is not a number");
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
