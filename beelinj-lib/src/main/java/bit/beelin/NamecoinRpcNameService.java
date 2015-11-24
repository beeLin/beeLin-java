package bit.beelin;

import com.msgilligan.bitcoinj.rpc.JsonRPCStatusException;
import com.msgilligan.namecoinj.core.NMCMainNetParams;
import com.msgilligan.namecoinj.json.pojo.NameData;
import com.msgilligan.namecoinj.rpc.NamecoinClient;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * This class implements a Name Service Provider, which Java can use
 * (starting with version 1.4), to perform DNS resolutions instead of using
 * the standard calls. Starting with Java 1.7 (which this implementation requires)
 * the resolvers are chained, so that if the first resolver in the chain
 * throws UnknownHostException, the next resolver will be called.
 * <p>
 * This Name Service Provider uses Namecoin JSON-RPC to resolve .bit domains.
 * <p>
 * To use this provider, you must set the following system property:
 * <b>sun.net.spi.nameservice.provider.1=namecoin-rpc,beelin</b>
 *
 * See http://docs.oracle.com/javase/7/docs/technotes/guides/net/properties.html and
 * http://rkuzmik.blogspot.ca/2006/08/local-managed-dns-java_11.html
 *
 */
public class NamecoinRpcNameService extends NamecoinNameService {
    public static String rpcName = "msgnmc";
    public static String rpcPassword = "msgnmc2015";
    private NamecoinClient namecoinClient;

    public NamecoinRpcNameService() {
        URI server = null;
        try {
            server = new URI("http://localhost:8336");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        NetworkParameters netParams = NMCMainNetParams.get();
        namecoinClient = new NamecoinClient(netParams, server, rpcName, rpcPassword);
    }

    @Override
    protected InetAddress[] resolveNamecoin(String domain) throws UnknownHostException {
        NameData result = null;
        try {
            result = namecoinClient.nameShow("d/" + domain);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonRPCStatusException e) {
            e.printStackTrace();
        }
        NamecoinValue data = new NamecoinValue(result.getValue());
        return data.getAddresses();
    }
}
