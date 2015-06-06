package fr.jcgay.notification.notifier.notificationcenter

import spock.lang.Specification
import spock.lang.Unroll

class TerminalNotifierConfigurationSpec extends Specification {

    @Unroll
    def "should build default configuration when properties are [#empty]"() {
        when:
        def result = TerminalNotifierConfiguration.create(empty)

        then:
        result.bin() == TerminalNotifierConfiguration.byDefault().bin()
        result.activateApplication() == TerminalNotifierConfiguration.byDefault().activateApplication()
        result.sound() == TerminalNotifierConfiguration.byDefault().sound()

        where:
        empty << [null, new Properties()]
    }

    def "should build user configuration"() {
        given:
        Properties properties = [
                'notifier.notification-center.path':'new path',
                'notifier.notification-center.activate':'com.googlecode.iterm2',
                'notifier.notification-center.sound':'default'
        ]

        when:
        def result = TerminalNotifierConfiguration.create(properties)

        then:
        result.bin() == 'new path'
        result.activateApplication() == 'com.googlecode.iterm2'
        result.sound() == 'default'
    }
}
