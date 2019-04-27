package fr.jcgay.notification.configuration

import spock.lang.Specification

class ConfigurationReaderSpec extends Specification {

    def "should read configuration from provided path"() {
        given:
        def reader = ConfigurationReader.atUrl(getClass().getResource("/configuration/example.properties").toURI().toURL())

        when:
        def result = reader.get()

        then:
        result['growl.host'] == 'localhost'
        result['growl.port'] == '12057'
    }

    def "should return empty configuration when reading configuration fails"() {
        given:
        def reader = ConfigurationReader.atUrl(new URL('file://unreachable-file'))

        when:
        def result = reader.get()

        then:
        result.isEmpty()
    }

    def "should return empty configuration when reading configuration from String fails"() {
        given:
        def reader = ConfigurationReader.atPath("unreachable-file")

        when:
        def result = reader.get()

        then:
        result.isEmpty()
    }
}
