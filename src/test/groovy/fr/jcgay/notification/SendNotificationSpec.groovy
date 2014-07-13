package fr.jcgay.notification

import fr.jcgay.notification.configuration.ConfigurationReader
import fr.jcgay.notification.notifier.growl.GrowlNotifier
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifier
import fr.jcgay.notification.notifier.notifysend.NotifySendNotifier
import fr.jcgay.notification.notifier.pushbullet.PushbulletNotifier
import fr.jcgay.notification.notifier.snarl.SnarlNotifier
import fr.jcgay.notification.notifier.systemtray.SystemTrayNotifier
import spock.lang.Specification

class SendNotificationSpec extends Specification {

    SendNotification sendNotification
    Properties properties = new Properties()

    void setup() {
        sendNotification = new SendNotification(new ConfigurationReader(properties))
    }

    def "should return notifier defined in user configuration"() {

        setup:
        properties << ['notifier.implementation': notifier, 'notifier.pushbullet.apikey': 'apikey']

        when:
        def result = sendNotification.chooseNotifier()

        then:
        implementation.isInstance(result)

        where:
        notifier             | implementation
        'growl'              | GrowlNotifier
        'notificationcenter' | TerminalNotifier
        'notifysend'         | NotifySendNotifier
        'pushbullet'         | PushbulletNotifier
        'snarl'              | SnarlNotifier
        'systemtray'         | SystemTrayNotifier
    }

    def "should return notifier when setting override notifier"() {

        setup:
        properties << ['notifier.pushbullet.apikey': 'apikey']

        when:
        def result = sendNotification.setChosenNotifier(notifier).chooseNotifier()

        then:
        implementation.isInstance(result)

        where:
        notifier             | implementation
        'growl'              | GrowlNotifier
        'notificationcenter' | TerminalNotifier
        'notifysend'         | NotifySendNotifier
        'pushbullet'         | PushbulletNotifier
        'snarl'              | SnarlNotifier
        'systemtray'         | SystemTrayNotifier
    }
}
