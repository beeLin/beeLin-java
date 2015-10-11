package bit.beelin;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

/**
 * The descriptor class for DNSChainNameService
 */
public class DNSChainNameServiceDescriptor implements NameServiceDescriptor {
    private static NameService nameService = new DNSChainNameService();

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
        return "namecoin";
    }
}
