package bit.beelin;

import sun.net.spi.nameservice.NameService;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Delegate to another name service (currently RPC client)
 * Future versions will be switchable via a preferences object.
 */
public class BeelinSwitchableNameService implements NameService {
    private NameService activeNameService = new NamecoinRpcNameService();

    @Override
    public InetAddress[] lookupAllHostAddr(String s) throws UnknownHostException {
        return activeNameService.lookupAllHostAddr(s);
    }

    @Override
    public String getHostByAddr(byte[] bytes) throws UnknownHostException {
        return activeNameService.getHostByAddr(bytes);
    }
}
