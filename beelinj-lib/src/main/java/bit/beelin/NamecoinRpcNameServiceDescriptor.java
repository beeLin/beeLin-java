package bit.beelin;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

/**
 * The descriptor class for NamecoinRPCNameService
 */
public class NamecoinRpcNameServiceDescriptor implements NameServiceDescriptor {
    private static NameService nameService = new NamecoinRpcNameService();

    @Override
    public NameService createNameService() throws Exception {
        return nameService;
    }

    @Override
    public String getProviderName() {
        return "beelin";
    }

    @Override
    public String getType() {
        return "namecoin-rpc";
    }
}
