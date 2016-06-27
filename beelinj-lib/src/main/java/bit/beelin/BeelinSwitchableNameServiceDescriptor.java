package bit.beelin;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

/**
 *
 */
public class BeelinSwitchableNameServiceDescriptor implements NameServiceDescriptor {
    private static NameService nameService = new BeelinSwitchableNameService();

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
        return "beelin-switchable";
    }
}
