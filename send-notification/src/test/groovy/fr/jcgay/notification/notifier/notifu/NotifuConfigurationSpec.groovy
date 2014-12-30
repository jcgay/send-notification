package fr.jcgay.notification.notifier.notifu

import spock.lang.Specification

class NotifuConfigurationSpec extends Specification {

    def "should build default configuration"() {

        when:
        def result = NotifuConfiguration.create(input)

        then:
        result.bin() == NotifuConfiguration.byDefault().bin()

        where:
        input << [null, new Properties()]
    }

    def "should build user configuration"() {

        given:
        Properties properties = [
                'notifier.notifu.path':'new path',
        ]

        when:
        def result = NotifuConfiguration.create(properties)

        then:
        result.bin() == 'new path'
    }
}
