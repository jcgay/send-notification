package fr.jcgay.notification

import spock.lang.Specification

import static fr.jcgay.notification.Notification.Level.ERROR
import static fr.jcgay.notification.Notification.Level.INFO


class NotificationSpec extends Specification {

    private final Icon icon = Icon.create(new URL('file:/img'), 'id')

    def "should build notification"() {
        when:
        def notification = Notification.builder('title', 'message', icon)
                .subtitle('subtitle')
                .level(Notification.Level.ERROR)
                .build()

        then:
        notification.title() == 'title'
        notification.message() == 'message'
        notification.subtitle() == 'subtitle'
        notification.icon() == icon
        notification.level() == ERROR
    }

    def "should build notification with type INFO by default"() {
        when:
        def notification = Notification.builder('title', 'message', icon).build()

        then:
        notification.level() == INFO
    }
}
