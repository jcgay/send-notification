package fr.jcgay.notification

import spock.lang.Specification


class NotificationSpec extends Specification {

    def "should build notification"() {

        when:
        def icon = Icon.create(new URL('file:/img'), 'id')
        def notification = Notification.builder('title', 'message', icon)
                .withSubtitle('subtitle')
                .build()

        then:
        notification.title() == 'title'
        notification.message() == 'message'
        notification.subtitle() == 'subtitle'
        notification.icon() == icon
    }
}
