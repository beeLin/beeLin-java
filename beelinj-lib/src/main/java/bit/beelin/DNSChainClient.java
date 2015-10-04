package bit.beelin;

import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.OkHttpClient;
import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A bare-bones starting point for a JVM DNSChain client.
 */
public class DNSChainClient {
    static final String dnsChainBase = "https://api.dnschain.net";
    static final int CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s
    static final int READ_TIMEOUT_MILLIS = 20 * 1000; // 20s
    private DNSChainService dnsChainService;

    interface DNSChainService {
        @GET("/v1/{chain}/key/{key}")
        Call<Map<String, Object>> lookup(@Path("chain") String chain, @Path("key") String key);
    }

    public DNSChainClient() {
        OkHttpClient client = initClient();

        Retrofit restAdapter = new Retrofit.Builder()
                .client(client)
                .baseUrl(dnsChainBase)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        //      .setLogLevel(Retrofit.LogLevel.FULL)    // Don't know how to do this in Retrofit 2.0

        dnsChainService = restAdapter.create(DNSChainService.class);
    }

    Map<String, Object>  lookupNamecoin(String domain) {
        Map<String, Object> result = null;
        try {
            result = dnsChainService.lookup("namecoin", "d/" + domain).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    String resolveNamecoin(String hostname) {
        String ipAddressString;
        Map<String, Object> result = lookupNamecoin(hostname);
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        Map<String, Object> value = (Map<String, Object>) data.get("value");
        Object ip = value.get("ip");
        if (ip instanceof String) {
            ipAddressString = (String) ip;
        } else if (ip instanceof List) {
            ipAddressString = (String) ((List) ip).get(0);
        } else {
            ipAddressString = "unknown type in JSON";
        }
        return ipAddressString;
    }

    private OkHttpClient initClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setCertificatePinner(
                new CertificatePinner.Builder()
                        // Pinning for DNSChain API Server -- is this right?
                        .add("api.dnschain.net", "sha1/OmfEeJ94QcdL+YrCl2bMp6Zh9LI=")
                        .add("api.dnschain.net", "sha1/KqqJgAYLy9ogXOWETcR36ioKf20=")
                        .build());
        return client;
    }
}
