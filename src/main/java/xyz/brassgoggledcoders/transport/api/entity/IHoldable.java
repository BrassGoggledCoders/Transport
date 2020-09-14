package xyz.brassgoggledcoders.transport.api.entity;

/**
 * An Interface for Minecarts which are held/released by the Holding Rail. ModuleInstances can also implement it it as well.
 *
 * EngineModuleInstance implements it by default to allow it to change PoweredState
 *
 * This is called for every tick a Minecart is interacting with a Holding rail
 */
public interface IHoldable {
    void onHeld();

    void onRelease();

    void push(float xPush, float zPush);
}
