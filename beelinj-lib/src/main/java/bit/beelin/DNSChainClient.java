package bit.beelin;

import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Path;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A bare-bones starting point for a JVM DNSChain client.
 * <p/>
 * Replaces the Groovy version.
 */
public class DNSChainClient {
    static final String dnsChainBase = "https://api.dnschain.net";
    static final int CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s
    static final int READ_TIMEOUT_MILLIS = 20 * 1000; // 20s
    private DNSChainService dnsChainService;

    interface DNSChainService {
        @GET("/v1/{chain}/key/{key}")
        Map lookup(@Path("chain") String chain, @Path("key") String key);
    }

    public DNSChainClient() {
        OkClient client = initClient();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(client)
                .setEndpoint(dnsChainBase)
                .build();

        dnsChainService = restAdapter.create(DNSChainService.class);
    }

    Map lookupNamecoin(String domain) throws IOException {
        return dnsChainService.lookup("namecoin", "d/" + domain);
    }

    private OkClient initClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setCertificatePinner(
                new CertificatePinner.Builder()
                        // Pinning for DNSChain API Server -- is this right?
                        .add("api.dnschain.net", "sha1/OmfEeJ94QcdL+YrCl2bMp6Zh9LI=")
                        .add("api.dnschain.net", "sha1/KqqJgAYLy9ogXOWETcR36ioKf20=")
                        .build());
        OkClient retrofitClient = new OkClient(client);
        return retrofitClient;
    }
}
