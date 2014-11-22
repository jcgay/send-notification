package fr.jcgay.notification.notifier.growl

import spock.lang.Specification


class GrowlConfigurationSpec extends Specification {

    def "should build default configuration"() {

        when:
        def result = GrowlConfiguration.create(input)

        then:
        result.host() == GrowlConfiguration.byDefault().host()
        result.password() == GrowlConfiguration.byDefault().password()
        result.port() == GrowlConfiguration.byDefault().port()

        where:
        input << [null, new Properties()]
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
