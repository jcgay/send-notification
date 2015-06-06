package fr.jcgay.notification.notifier.notifu

import spock.lang.Specification
import spock.lang.Unroll

class NotifuConfigurationSpec extends Specification {

    @Unroll
    def "should build default configuration when properties are [#empty]"() {
        when:
        def result = NotifuConfiguration.create(empty)

        then:
        result.bin() == NotifuConfiguration.byDefault().bin()

        where:
        empty << [null, new Properties()]
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
