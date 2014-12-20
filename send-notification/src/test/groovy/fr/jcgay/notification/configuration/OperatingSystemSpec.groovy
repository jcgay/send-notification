package fr.jcgay.notification.configuration

import spock.lang.Specification

class OperatingSystemSpec extends Specification {

    def "should detect when current host is a mac"() {

        setup:
        def currentOs = new OperatingSystem(os)

        expect:
        currentOs.isMac() == result

        where:
        os           | result
        'Mac OS X'   | true
        'Windows XP' | false
        'Linux'      | false
    }

    def "should detect when current host is running windows"() {

        setup:
        def currentOs = new OperatingSystem(os)

        expect:
        currentOs.isWindows() == result

        where:
        os           | result
        'Mac OS X'   | false
        'Windows XP' | true
        'Linux'      | false
    }
}
