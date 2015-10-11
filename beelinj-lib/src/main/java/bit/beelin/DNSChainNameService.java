package bit.beelin;

import com.google.common.net.InternetDomainName;
import sun.net.spi.nameservice.NameService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a Name Service Provider, which Java can use
 * (starting with version 1.4), to perform DNS resolutions instead of using
 * the standard calls. Starting with Java 1.7 (which this implementation requires)
 * the resolvers are chained, so that if the first resolver in the chain
 * throws UnknownHostException, the next resolver will be called.
 * <p>
 * This Name Service Provider uses DNSChain to resolve .bit domains.
 * <p>
 * To use this provider, you must set the following system property:
 * <b>sun.net.spi.nameservice.provider.1=namecoin,beelin</b>
 *
 * See http://docs.oracle.com/javase/7/docs/technotes/guides/net/properties.html and
 * http://rkuzmik.blogspot.ca/2006/08/local-managed-dns-java_11.html
 *
 */
public class DNSChainNameService implements NameService {
    private DNSChainClient dnsChainClient = new DNSChainClient();

    /**
     * @see sun.net.spi.nameservice.NameService#getHostByAddr(byte[])
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
        return dnsChainClient.resolveNamecoin(domain);
    }

    /**
     *  Reverse lookup (get hostname from address)
     *
     *  @see sun.net.spi.nameservice.NameService#lookupAllHostAddr(java.lang.String)
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
