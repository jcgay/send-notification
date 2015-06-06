package fr.jcgay.notification.notifier.notifysend

import spock.lang.Specification
import spock.lang.Unroll

class NotifySendConfigurationSpec extends Specification {

    @Unroll
    def "should build default configuration when properties are [#empty]"() {
        when:
        def result = NotifySendConfiguration.create(empty)

        then:
        result.bin() == NotifySendConfiguration.byDefault().bin()

        where:
        empty << [null, new Properties()]
    }

    def "should build user configuration"() {
        given:
        Properties properties = ['notifier.notify-send.path':'new path']

        when:
        def result = NotifySendConfiguration.create(properties)

        then:
        result.bin() == 'new path'
    }
}
