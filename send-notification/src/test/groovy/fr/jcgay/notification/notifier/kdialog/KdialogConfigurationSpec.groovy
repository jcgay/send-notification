package fr.jcgay.notification.notifier.kdialog

import spock.lang.Specification

class KdialogConfigurationSpec extends Specification {

    def "should build default configuration"() {

        when:
        def result = KdialogConfiguration.create(input)

        then:
        result.bin() == KdialogConfiguration.byDefault().bin()

        where:
        input << [null, new Properties()]
    }

    def "should build user configuration"() {

        given:
        Properties properties = [
                'notifier.kdialog.path':'new path',
        ]

        when:
        def result = KdialogConfiguration.create(properties)

        then:
        result.bin() == 'new path'
    }
}
