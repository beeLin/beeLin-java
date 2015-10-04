package bit.beelin

import spock.lang.Specification

import com.squareup.okhttp.HttpUrl;


/**
 * Let's test OkHttp's  HttpUrl with .bit URLs
 */
class OkHttpUrl extends Specification {

    def "can parse .bit URLs"() {
        when:
        def url = HttpUrl.parse("https://beelin.bit");

        then:
        url.scheme() == "https"
        url.host() == "beelin.bit";
    }

}