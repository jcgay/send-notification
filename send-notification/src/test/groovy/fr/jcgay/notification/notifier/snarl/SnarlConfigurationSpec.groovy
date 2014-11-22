package fr.jcgay.notification.notifier.snarl

import spock.lang.Specification

class SnarlConfigurationSpec extends Specification {

    def "should build default configuration"() {

        when:
        def result = SnarlConfiguration.create(input)

        then:
        result.host() == SnarlConfiguration.byDefault().host()
        result.port() == SnarlConfiguration.byDefault().port()

        where:
        input << [null, new Properties()]
    }

    def "should build user configuration"() {

        given:
        Properties properties = [
                'notifier.snarl.host':'host',
                'notifier.snarl.port':'12345'
        ]

        when:
        def result = SnarlConfiguration.create(properties)

        then:
        result.host() == 'host'
        result.port() == 12345
    }
}
