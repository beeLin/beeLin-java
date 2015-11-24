package bit.beelin;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class NamecoinValue {
    private Map<String, Object> value;

    public NamecoinValue(Map<String, Object> value) {
        this.value = value;
    }

    public String getAlias() {
        Object alias = value.get("alias");
        if ((alias instanceof String) && ((String) alias).endsWith(".")) {
            // If there's an alias to an absolute domain, return it as String
            return (String) alias;
        }
        return null;
    }

    public List<String> getIps() {
        List<String> ips = new ArrayList<String>();
        Object ip = value.get("ip");
        if (ip instanceof String) {
            ips.add((String) ip);
        } else if (ip instanceof List) {
            for (String addr : (List<String>) ip) {
                ips.add(addr);
            }
        } else if (ip == null) {
            throw new RuntimeException("'data.value.ip' is null in JSON result");
        } else {
            throw new RuntimeException("'data.value.ip' is unknown type in JSON result");
        }
        return ips;
    }

    public InetAddress[] getAddresses() throws UnknownHostException {
        List<InetAddress> addresses = new ArrayList<>(1);
        String alias = getAlias();
        if (alias != null) {
            // If there's an alias to an absolute domain, use it to get InetAddress[]
            return InetAddress.getAllByName(alias);
        }
        List<String> ips = getIps();
        try {
            for (String address : ips) {
                addresses.add(InetAddress.getByName(address));
            }
        } catch (UnknownHostException e) {
            // Should never happen since we're only using already resolved IP address literal
            throw new RuntimeException(e);
        }
        return addresses.toArray(new InetAddress[addresses.size()]);
    }
}
