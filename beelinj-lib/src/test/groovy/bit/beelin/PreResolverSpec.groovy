package bit.beelin

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


/**
 *
 */
class PreResolverSpec extends Specification {
    @Shared PreResolver resolver

    @Unroll
    def "#input maps to #expectedOutput"() {
        when:
        def output = resolver.resolve(input)

        then:
        output == expectedOutput

        where:
        input                       | expectedOutput
        "http://okturtles.com"      | "http://okturtles.com"
        "https://okturtles.com"     | "https://okturtles.com"
        "http://okturtles.bit"      | "http://192.184.93.146:80/"
        "https://okturtles.bit"     | "https://192.184.93.146:443/"
        "http://msgilligan.com"     | "http://msgilligan.com"
        "https://msgilligan.com"    | "https://msgilligan.com"
        "http://msgilligan.bit"     | "http://207.111.216.146:80/"
        "https://msgilligan.bit"    | "https://207.111.216.146:443/"
    }

    def setup() {
        resolver = new PreResolver()
    }
}