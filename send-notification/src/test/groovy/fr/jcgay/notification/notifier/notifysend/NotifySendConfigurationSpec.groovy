package fr.jcgay.notification.notifier.notifysend

import spock.lang.Specification

class NotifySendConfigurationSpec extends Specification {

    def "should build default configuration"() {

        when:
        def result = NotifySendConfiguration.create(input)

        then:
        result.bin() == NotifySendConfiguration.byDefault().bin()

        where:
        input << [null, new Properties()]
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
