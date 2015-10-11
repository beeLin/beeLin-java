package bit.beelin

import spock.lang.Shared
import spock.lang.Specification
import java.net.UnknownHostException;


/**
 *
 */
class DNSChainNameServiceSpec extends Specification {
    @Shared DNSChainNameService service

    def "can lookup .bit hostnames"() {
        when:
        InetAddress[] ip = service.lookupAllHostAddr("okturtles.bit")

        then:
        ip.length == 1
        ip[0] == InetAddress.getByName("192.184.93.146")
    }

    def "throws exception when not a .bit domain, passing responsibility to next in chain"() {
        when:
        InetAddress[] ip = service.lookupAllHostAddr("okturtles.com")

        then:
        UnknownHostException e = thrown()

    }

    def "throws exception on reverse lookup, passing responsibility to next in chain"() {
        when:
        String hostname = service.getHostByAddr(InetAddress.getByName("192.184.93.146").address)

        then:
        UnknownHostException e = thrown()
    }

    def setup() {
        service = new DNSChainNameService()
    }
}