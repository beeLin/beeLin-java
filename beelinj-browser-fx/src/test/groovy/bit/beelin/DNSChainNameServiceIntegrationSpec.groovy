package bit.beelin

import spock.lang.Specification


/**
 *
 */
class DNSChainNameServiceIntegrationSpec extends Specification {

    def "make sure system properties are set correctly in Gradle or in IDE" () {
        expect:
        System.getProperty('sun.net.spi.nameservice.provider.1') == 'namecoin,beelin'
        System.getProperty('sun.net.spi.nameservice.provider.2') == 'default'
    }

    def "DNSChainNameService is registered"() {
        when: "we check a private field in InetAddress"
        def service = InetAddress.nameServices.get(0)

        then: "our service is there"
        service instanceof DNSChainNameService
    }

    def "integration test of DNSChainNameService"() {
        when:
        def address = InetAddress.getByName("okturtles.bit")

        then:
        address == InetAddress.getByName("192.184.93.146")
    }

}