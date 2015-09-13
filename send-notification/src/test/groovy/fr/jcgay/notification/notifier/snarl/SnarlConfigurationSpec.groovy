package fr.jcgay.notification.notifier.snarl

import spock.lang.Specification
import spock.lang.Unroll

class SnarlConfigurationSpec extends Specification {

    @Unroll
    def "should build default configuration when properties are [#empty]"() {
        when:
        def result = SnarlConfiguration.create(empty)

        then:
        result.host() == SnarlConfiguration.byDefault().host()
        result.port() == SnarlConfiguration.byDefault().port()
        result.applicationPassword() == SnarlConfiguration.byDefault().applicationPassword()

        where:
        empty << [null, new Properties()]
    }

    def "should build user configuration"() {
        given:
        Properties properties = [
                'notifier.snarl.host':'host',
                'notifier.snarl.port':'12345',
                'notifier.snarl.appPassword':'app-password'
        ]

        when:
        def result = SnarlConfiguration.create(properties)

        then:
        result.host() == 'host'
        result.port() == 12345
        result.applicationPassword() == 'app-password'
    }
}
