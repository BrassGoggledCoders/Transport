package xyz.brassgoggledcoders.transport.compat.theoneprobe;

import mcjty.theoneprobe.api.NumberFormat;

import java.text.DecimalFormat;

public class TheOneProbeHelper {
    private static final DecimalFormat COMMAS = new DecimalFormat("###,###");

    public static String format(long in, NumberFormat style, String suffix) {
        switch (style) {
            case FULL:
                return in + suffix;
            case COMPACT: {
                int unit = 1000;
                if (in < unit) {
                    return in + " " + suffix;
                }
                int exp = (int) (Math.log(in) / Math.log(unit));
                char pre;
                if (suffix.startsWith("m")) {
                    suffix = suffix.substring(1);
                    if (exp - 2 >= 0) {
                        pre = "kMGTPE".charAt(exp - 2);
                        return String.format("%.1f %s", in / Math.pow(unit, exp), pre) + suffix;
                    } else {
                        return String.format("%.1f %s", in / Math.pow(unit, exp), suffix);
                    }
                } else {
                    pre = "kMGTPE".charAt(exp - 1);
                    return String.format("%.1f %s", in / Math.pow(unit, exp), pre) + suffix;
                }
            }
            case COMMAS:
                return COMMAS.format(in) + suffix;
            case NONE:
                return suffix;
        }
        return Long.toString(in);
    }
}
