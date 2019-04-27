package fr.jcgay.notification.notifier.growl

import spock.lang.Specification
import spock.lang.Unroll


class GrowlConfigurationSpec extends Specification {

    @Unroll
    def "should build default configuration when properties are [#empty]"() {
        when:
        def result = GrowlConfiguration.create(empty)

        then:
        result.host() == GrowlConfiguration.byDefault().host()
        result.password() == GrowlConfiguration.byDefault().password()
        result.port() == GrowlConfiguration.byDefault().port()

        where:
        empty << [null, new Properties()]
    }

    def "should build user configuration"() {

        given:
        Properties properties = [
                'notifier.growl.host':'host',
                'notifier.growl.password':'password',
                'notifier.growl.port':'12345'
        ]

        when:
        def result = GrowlConfiguration.create(properties)

        then:
        result.host() == 'host'
        result.password() == 'password'
        result.port() == 12345
    }
}
