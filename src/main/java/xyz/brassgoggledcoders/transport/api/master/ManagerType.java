package xyz.brassgoggledcoders.transport.api.master;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class ManagerType {
    private static final Pattern VALID_NAME = Pattern.compile("[^a-z_]");
    private static final Map<String, ManagerType> VALUES = new ConcurrentHashMap<>();

    public static final ManagerType RAIL = get("rail");

    public static ManagerType get(String name) {
        return VALUES.computeIfAbsent(name, k -> {
            if (VALID_NAME.matcher(name).find()) {
                throw new IllegalArgumentException("ManagerType.get() called with invalid name: " + name);
            }
            return new ManagerType(name);
        });
    }

    private final String name;

    private ManagerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
