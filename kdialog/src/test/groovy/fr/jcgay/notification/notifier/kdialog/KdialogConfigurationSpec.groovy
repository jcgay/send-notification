package fr.jcgay.notification.notifier.kdialog

import spock.lang.Specification
import spock.lang.Unroll

class KdialogConfigurationSpec extends Specification {

    @Unroll
    def "should build default configuration when properties are [#empty]"() {
        when:
        def result = KdialogConfiguration.create(empty)

        then:
        result.bin() == KdialogConfiguration.byDefault().bin()

        where:
        empty << [null, new Properties()]
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
