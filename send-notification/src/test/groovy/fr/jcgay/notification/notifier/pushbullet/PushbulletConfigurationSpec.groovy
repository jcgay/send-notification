package fr.jcgay.notification.notifier.pushbullet

import spock.lang.Specification


class PushbulletConfigurationSpec extends Specification {

    def "should throw NPE when creating configuration without properties"() {
        when:
        PushbulletConfiguration.create(null)

        then:
        def result = thrown(NullPointerException)
        result.message == 'Cannot create Pushbullet configuration without user configuration.'
    }

    def "should fail when creating configuration without defining api key"() {
        when:
        PushbulletConfiguration.create(new Properties())

        then:
        thrown(NullPointerException)
    }

    def "should build user configuration"() {
        given:
        Properties properties = [
                'notifier.pushbullet.apikey':'key',
                'notifier.pushbullet.device':'12345',
                'notifier.pushbullet.email':'jcgay@does-not-exist.com',
        ]

        when:
        def result = PushbulletConfiguration.create(properties)

        then:
        result.key() == 'key'
        result.device() == '12345'
        result.email() == 'jcgay@does-not-exist.com'
    }

    def "should build user configuration without device"() {
        given:
        Properties properties = ['notifier.pushbullet.apikey': 'key']

        when:
        def result = PushbulletConfiguration.create(properties)

        then:
        result.device() == null
    }

    def "should build user configuration without email"() {
        given:
        Properties properties = ['notifier.pushbullet.apikey': 'key']

        when:
        def result = PushbulletConfiguration.create(properties)

        then:
        result.email() == null
    }
}
