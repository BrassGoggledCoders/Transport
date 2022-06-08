package xyz.brassgoggledcoders.transport.functional;

import java.util.function.Consumer;

public abstract class Trial<T> {
    public abstract void match(Consumer<T> onSuccess, Consumer<String> onWarn);

    public static <U> Success<U> success(U value) {
        return new Success<>(value);
    }

    public static <U> Warn<U> warn(String warn) {
        return new Warn<>(warn);
    }

    public static class Success<U> extends Trial<U> {
        private final U value;

        public Success(U value) {
            this.value = value;
        }

        @Override
        public void match(Consumer<U> onSuccess, Consumer<String> onWarn) {
            onSuccess.accept(value);
        }
    }

    public static class Warn<U> extends Trial<U> {
        private final String warn;

        public Warn(String warn) {
            this.warn = warn;
        }

        @Override
        public void match(Consumer<U> onSuccess, Consumer<String> onWarn) {
            onWarn.accept(warn);
        }
    }
}
