package bit.beelin;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

import java.io.IOException;
import java.util.Map;

/**
 * A bare-bones starting point for a JVM DNSChain client.
 * <p/>
 * Replaces the Groovy version.
 */
public class DNSChainClient {
    final String dnsChainBase = "http://api.dnschain.net";
    private DNSChainService dnsChainService;

    interface DNSChainService {
        @GET("/v1/{chain}/key/{key}")
        Map lookup(@Path("chain") String chain, @Path("key") String key);
    }

    public DNSChainClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(dnsChainBase)
                .build();

        dnsChainService = restAdapter.create(DNSChainService.class);
    }

    Map lookupNamecoin(String domain) throws IOException {
        return dnsChainService.lookup("namecoin", "d/" + domain);
    }
}
