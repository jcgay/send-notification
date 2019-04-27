package fr.jcgay.notification.configuration

import spock.lang.Specification
import spock.lang.Unroll

class OperatingSystemSpec extends Specification {

    @Unroll
    def "should return true when #os is a mac"() {
        expect:
        new OperatingSystem(os).isMac() == result

        where:
        os           | result
        'Mac OS X'   | true
        'Windows XP' | false
        'Linux'      | false
    }

    @Unroll
    def "should return true when #os is windows"() {
        expect:
        new OperatingSystem(os).isWindows() == result

        where:
        os           | result
        'Mac OS X'   | false
        'Windows XP' | true
        'Linux'      | false
    }
}
