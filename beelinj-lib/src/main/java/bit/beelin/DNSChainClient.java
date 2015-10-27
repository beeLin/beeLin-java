package bit.beelin;

import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.OkHttpClient;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.JacksonConverterFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

    public Map<String, Object>  lookupNamecoin(String domain) throws UnknownHostException {
        Response<Map<String, Object>> response;
        Map<String, Object> result = null;
        try {
            response = dnsChainService.lookup("namecoin", "d/" + domain).execute();
            int code = response.code();
            if (code == 404) {
                throw new UnknownHostException(domain);
            } else if (code != 200) {
                throw new RuntimeException("DNSChain lookup returned HTTP Error: " + code);
            }
            result = response.body();
        } catch (UnknownHostException uhe) {
            throw uhe; // Don't let our catch of IOException stop us from throwing UnknownHostException
        } catch (IOException ioe) {
            // Maybe we should remove the try/catch altogether and add IOException to our method signature
            ioe.printStackTrace();
            throw new RuntimeException(ioe);
        }
        return result;
    }

    public InetAddress[] resolveNamecoin(String hostname) throws UnknownHostException {
        List<InetAddress> addresses = new ArrayList<>(1);
        Map<String, Object> result = lookupNamecoin(hostname);
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        Map<String, Object> value = (Map<String, Object>) data.get("value");
        Object alias = value.get("alias");
        if ((alias instanceof String) && ((String) alias).endsWith(".")) {
            // If there's an alias to an absolute domain, use it to get InetAddress[]
            return InetAddress.getAllByName((String) alias);
        }
        Object ip = value.get("ip");
        try {
            if (ip instanceof String) {
                addresses.add(InetAddress.getByName((String) ip));
            } else if (ip instanceof List) {
                for (String addr : (List<String>) ip) {
                    addresses.add(InetAddress.getByName(addr));
                }
            } else if (ip == null) {
                throw new RuntimeException("'data.value.ip' is null in JSON result");
            } else {
                throw new RuntimeException("'data.value.ip' is unknown type in JSON result");
            }
        } catch (UnknownHostException e) {
            // Should never happen since we're only using already resolved IP address literal
            throw new RuntimeException(e);
        }
        return addresses.toArray(new InetAddress[addresses.size()]);
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
