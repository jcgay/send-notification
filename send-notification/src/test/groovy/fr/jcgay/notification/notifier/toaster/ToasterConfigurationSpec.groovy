package fr.jcgay.notification.notifier.toaster

import spock.lang.Specification
import spock.lang.Unroll

class ToasterConfigurationSpec extends Specification {

    @Unroll
    def "should build default configuration when properties are [#empty]"() {
        when:
        def result = ToasterConfiguration.create(empty)

        then:
        result.bin() == ToasterConfiguration.byDefault().bin()

        where:
        empty << [null, new Properties()]
    }

    def "should build user configuration"() {
        given:
        Properties properties = [
            'notifier.toaster.bin':/C:\Program Files\Toaster\toaster.exe/
        ]

        when:
        def result = ToasterConfiguration.create(properties)

        then:
        result.bin() == /C:\Program Files\Toaster\toaster.exe/
    }
}
