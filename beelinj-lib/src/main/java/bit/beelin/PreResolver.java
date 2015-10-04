package bit.beelin;

import com.google.common.net.InternetDomainName;
import com.squareup.okhttp.HttpUrl;

import java.util.List;
import java.util.Map;

/**
 * Pre-process resolver that resolves beeLin URLs into legacy URLs
 *
 * For example a `.bit` URL may resolve to an IP address.
 *
 * For now this is a minimal wrapper around DNSChainClient, but in
 * the future it may select between multiple alternatives.
 *
 */
public class PreResolver {
    private DNSChainClient dnsChainClient = new DNSChainClient();

    public String resolve(String input) {
        String output;
        HttpUrl url = HttpUrl.parse(input);
        String host = url.host();
        InternetDomainName idn = InternetDomainName.from(host);
        String tld = getTld(idn);
        if (tld.equals("bit")) {
            List<String> parts = idn.parts();
            // For now assume only 2 parts.
            String ip = dnsChainClient.resolveNamecoin(parts.get(0));
            output = url.scheme() + "://" + ip + ":" + url.port() + url.encodedPath();
        } else {
            output = input;
        }
        return output;
    }

    private String getTld(InternetDomainName hostname) {
        InternetDomainName n = hostname;
        while (n.hasParent()) {
            n = n.parent();
        }
        return n.toString();
    }
}
