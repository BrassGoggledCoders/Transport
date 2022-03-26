package xyz.brassgoggledcoders.transport.shellcontent.storage.item;

import java.util.Optional;

public enum StorageSize {
    THREE_BY_NINE;

    public static Optional<StorageSize> getByName(String name) {
        for (StorageSize size : values()) {
            if (size.name().equalsIgnoreCase(name)) {
                return Optional.of(size);
            }
        }
        return Optional.empty();
    }
}
