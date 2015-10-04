package bit.beelin

import spock.lang.Shared
import spock.lang.Specification


/**
 * Test Spec for DNSChainClient
 */
class DNSChainClientSpec extends Specification {

    @Shared DNSChainClient client

    def "test lookup of okturtles"() {
        when:
        def json = client.lookupNamecoin("okturtles")

        then:
        json != null;
        json.version == "0.0.1"
        json.header.datastore == "namecoin"
        json.data.name == "d/okturtles"
        json.data.value.email == "hi@okturtles.com"
        json.data.value.ip[0] == "192.184.93.146"
        json.data.value.tls.sha1[0] == "5F:8B:74:78:4F:55:27:19:DC:53:6B:9B:C8:99:CD:91:8A:57:DD:07"
    }

    def "test lookup of msgilligan"() {
        when:
        def json = client.lookupNamecoin("msgilligan")

        then:
        json != null;
        json.version == "0.0.1"
        json.header.datastore == "namecoin"
        json.data.name == "d/msgilligan"
        json.data.value.ip == "207.111.216.146"
    }

    def "test resolve of okturtles"() {
        when:
        def ip = client.resolveNamecoin("okturtles")

        then:
        ip == "192.184.93.146"
    }

    def "test resolve of msgilligan"() {
        when:
        def ip = client.resolveNamecoin("msgilligan")

        then:
        ip == "207.111.216.146"
    }

    def setup() {
        client = new DNSChainClient()
    }
}