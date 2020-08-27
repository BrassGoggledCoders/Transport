package xyz.brassgoggledcoders.transport.api.functional;

public interface ThrowingFunction<T, U, E extends Exception> {
    U apply(T t) throws E;
}
