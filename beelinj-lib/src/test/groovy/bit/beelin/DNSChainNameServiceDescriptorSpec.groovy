package bit.beelin

import spock.lang.Shared
import spock.lang.Specification
import sun.net.spi.nameservice.NameService


/**
 *
 */
class DNSChainNameServiceDescriptorSpec extends Specification {
    @Shared DNSChainNameServiceDescriptor descriptor

    def "can create the service"() {
        when:
        def service = descriptor.createNameService()

        then:
        service instanceof NameService
    }

    def "name and provider are correct"() {
        expect:
        descriptor.getType() == "namecoin"
        descriptor.getProviderName() == "beelin"
    }


    def setup() {
        descriptor = new DNSChainNameServiceDescriptor()
    }

}