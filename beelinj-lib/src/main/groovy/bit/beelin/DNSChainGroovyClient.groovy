package bit.beelin

import groovy.json.JsonSlurper
import groovy.transform.CompileStatic;


/**
 * A bare-bones starting point for a JVM DNSChain client.
 *
 * @deprecated Rewritten in Java using Retrofit. Use the Java version instead.
 *
 */
@Deprecated
@CompileStatic
public class DNSChainGroovyClient {
    def dnsChainBase = "https://api.dnschain.net";

    /**
     * Lookup a .bit domain using DNSChain
     *
     * @param domain - .bit domain name (without the .bit suffix)
     * @return  JSON result from DNSChain server
     */
    Object lookupNamecoin(String domain) {
        def keyVal = "d%2F${domain}"  // '%2F' is a slash
        def url = new URL("${dnsChainBase}/v1/namecoin/key/${keyVal}")
        return new JsonSlurper().parse(url)
    }
}