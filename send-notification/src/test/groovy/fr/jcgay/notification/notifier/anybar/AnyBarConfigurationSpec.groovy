package fr.jcgay.notification.notifier.anybar

import spock.lang.Specification

class AnyBarConfigurationSpec extends Specification {

    def "should build default configuration"() {

        when:
        def result = AnyBarConfiguration.create(input)

        then:
        result.port() == AnyBarConfiguration.byDefault().port()
        result.host() == AnyBarConfiguration.byDefault().host()

        where:
        input << [null, new Properties()]
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
