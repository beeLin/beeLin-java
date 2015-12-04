package bit.beelin;

import com.google.common.net.InternetDomainName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class DNSChainNameService extends NamecoinNameService {
    private static final Logger log = LoggerFactory.getLogger(DNSChainNameService.class);
    private DNSChainClient dnsChainClient = new DNSChainClient();

    @Override
    protected InetAddress[] resolveNamecoin(String domain) throws UnknownHostException {
        InetAddress[] addresses = dnsChainClient.resolveNamecoin(domain);
        log.info("resolveNamecoin({}) -> {}", domain, addresses);
        return addresses;
    }
}
