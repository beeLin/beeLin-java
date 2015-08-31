package bit.beelin

import spock.lang.Specification


/**
 *
 */
class PinMakerSpec extends Specification {
    def "can convert fingerprint for api.dnschain.net"() {
        given:
        def fingerprint = "F3:36:DB:3E:41:CE:27:57:21:2F:1A:9B:D4:55:13:37:20:3A:DC:A2"

        when:
        def pin = PinMaker.colonHexToBase64Pin(fingerprint)

        then:
        pin == "sha1/8zbbPkHOJ1chLxqb1FUTNyA63KI="
    }

    def "can convert fingerprint for okturtles.net"() {
      given:
      def fingerprint = "5F:8B:74:78:4F:55:27:19:DC:53:6B:9B:C8:99:CD:91:8A:57:DD:07"

      when:
      def pin = PinMaker.colonHexToBase64Pin(fingerprint)

      then:
      pin == "sha1/X4t0eE9VJxncU2ubyJnNkYpX3Qc="
    }
}