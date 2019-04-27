package fr.jcgay.notification.notifier.anybar

import spock.lang.Specification
import spock.lang.Unroll

class AnyBarConfigurationSpec extends Specification {

    @Unroll
    def "should build default configuration when properties are [#empty]"() {
        when:
        def result = AnyBarConfiguration.create(empty)

        then:
        result.port() == AnyBarConfiguration.byDefault().port()
        result.host() == AnyBarConfiguration.byDefault().host()

        where:
        empty << [null, new Properties()]
    }

    def "should build user configuration"() {
        given:
        Properties properties = [
            'notifier.anybar.host':'hostname',
            'notifier.anybar.port':'12345'
        ]

        when:
        def result = AnyBarConfiguration.create(properties)

        then:
        result.port() == 12345
        result.host() == 'hostname'
    }
}
