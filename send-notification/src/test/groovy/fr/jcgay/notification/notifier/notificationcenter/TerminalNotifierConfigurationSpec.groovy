package fr.jcgay.notification.notifier.notificationcenter

import spock.lang.Specification

class TerminalNotifierConfigurationSpec extends Specification {

    def "should build default configuration"() {

        when:
        def result = TerminalNotifierConfiguration.create(input)

        then:
        result.bin() == TerminalNotifierConfiguration.byDefault().bin()
        result.activateApplication() == TerminalNotifierConfiguration.byDefault().activateApplication()

        where:
        input << [null, new Properties()]
    }

    def "should build user configuration"() {

        given:
        Properties properties = [
                'notifier.notification-center.path':'new path',
                'notifier.notification-center.activate':'com.googlecode.iterm2'
        ]

        when:
        def result = TerminalNotifierConfiguration.create(properties)

        then:
        result.bin() == 'new path'
        result.activateApplication() == 'com.googlecode.iterm2'
    }
}
