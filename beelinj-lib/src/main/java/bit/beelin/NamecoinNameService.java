package bit.beelin;

import com.google.common.net.InternetDomainName;
import sun.net.spi.nameservice.NameService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class NamecoinNameService implements NameService {
    private DNSChainClient dnsChainClient = new DNSChainClient();

    /**
     * @see NameService#getHostByAddr(byte[])
     */
    @Override
    public InetAddress[] lookupAllHostAddr(String host) throws UnknownHostException {
        List<InetAddress> addresses = new ArrayList<>(1);
        InternetDomainName idn = InternetDomainName.from(host);
        String tld = getTld(idn);
        if (!tld.equals("bit")) {
            // Not a .bit domain, fall back to standard resolver
            throw new UnknownHostException();
        }
        List<String> parts = idn.parts();

        if (parts.size() < 2) {
            throw new UnknownHostException();
        }

        // For now assume only 2 parts.
        String domain = parts.get(parts.size() - 2);
        return resolveNamecoin(domain);
    }

    protected abstract InetAddress[] resolveNamecoin(String domain) throws UnknownHostException;

    /**
     *  Reverse lookup (get hostname from address)
     *
     *  @see NameService#lookupAllHostAddr(String)
     */
    @Override
    public String getHostByAddr(byte[] bytes) throws UnknownHostException {
        // Can we use DNSChain for reverse lookups, should we?
        // For now, throw UnknownHostException which should cause a fallback to doing
        // reverse lookup with the next resolver in the chain.
        throw new UnknownHostException();
    }

    private String getTld(InternetDomainName hostname) {
        InternetDomainName n = hostname;
        while (n.hasParent()) {
            n = n.parent();
        }
        return n.toString();
    }
}
