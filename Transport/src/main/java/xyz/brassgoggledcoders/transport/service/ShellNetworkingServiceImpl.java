package xyz.brassgoggledcoders.transport.service;

import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.service.IShellNetworkingService;
import xyz.brassgoggledcoders.transport.api.shell.IShell;

public class ShellNetworkingServiceImpl implements IShellNetworkingService {
    @Override
    public void newGeneration(IShell shell) {
        Transport.NETWORK.sendNewGenerationMessage(shell);
    }
}
