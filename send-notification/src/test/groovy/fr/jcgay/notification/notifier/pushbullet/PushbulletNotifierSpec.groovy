package fr.jcgay.notification.notifier.pushbullet

import com.github.tomakehurst.wiremock.junit.WireMockRule
import fr.jcgay.notification.Application
import fr.jcgay.notification.Icon
import fr.jcgay.notification.Notification
import org.junit.Rule
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static fr.jcgay.notification.Notification.Level.ERROR

class PushbulletNotifierSpec extends Specification {

    @Rule
    WireMockRule wireMock = new WireMockRule()

    def "should fail when credentials are not valid"() {
        given:
        def icon = Icon.create(new URL('file:/icon.png'), 'icon')
        def application = Application.builder('id', 'name', icon).build()
        Properties configuration = [
            'notifier.pushbullet.apikey':'access-token-not-valid',
        ]

        and:
        def pushbullet = new PushbulletNotifier(application, PushbulletConfiguration.create(configuration), "http://localhost:${wireMock.port()}/pushes")
        pushbullet.init()

        and:
        wireMock.stubFor(post(urlEqualTo("/pushes")).willReturn(aResponse().withStatus(401)))

        when:
        pushbullet.send(Notification.builder('title', 'message', icon)
            .subtitle('subtitle')
            .level(ERROR)
            .build())

        then:
        def result = thrown(PushbulletNotificationException)
        result.message == 'Pushbullet notification has failed, [401] - [Unauthorized]\n\nCheck your configuration at: https://github.com/jcgay/send-notification/wiki/Pushbullet'
    }
}
