package fr.jcgay.notification

import fr.jcgay.notification.configuration.ConfigurationReader
import fr.jcgay.notification.configuration.OperatingSystem
import fr.jcgay.notification.notifier.DoNothingNotifier
import fr.jcgay.notification.notifier.growl.GrowlNotifier
import fr.jcgay.notification.notifier.kdialog.KdialogNotifier
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifier
import fr.jcgay.notification.notifier.notifu.NotifuNotifier
import fr.jcgay.notification.notifier.notifysend.NotifySendNotifier
import fr.jcgay.notification.notifier.pushbullet.PushbulletNotifier
import fr.jcgay.notification.notifier.snarl.SnarlNotifier
import fr.jcgay.notification.notifier.systemtray.SystemTrayNotifier
import spock.lang.Specification

class SendNotificationSpec extends Specification {

    SendNotification sendNotification
    Properties properties = new Properties()

    void setup() {
        sendNotification = new SendNotification(new ConfigurationReader(properties), new OperatingSystem())
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
        'notifu'             | NotifuNotifier
        'kdialog'            | KdialogNotifier
        'unknown'            | DoNothingNotifier
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
        'notifu'             | NotifuNotifier
        'kdialog'            | KdialogNotifier
        'unknown'            | DoNothingNotifier
    }

    def "should return default notifier when no configuration (file or manually set) is present"() {

        setup:
        sendNotification = new SendNotification(new ConfigurationReader(properties), new OperatingSystem(currentOs))

        when:
        def result = sendNotification.chooseNotifier()

        then:
        implementation.isInstance(result)

        where:
        currentOs    | implementation
        'Mac OS X'   | GrowlNotifier
        'Windows XP' | GrowlNotifier
        'Linux'      | NotifySendNotifier
    }

    def "should override configuration when adding properties"() {

        setup:
        properties << ['notifier.implementation': 'growl', 'notifier.pushbullet.apikey': 'apikey']
        sendNotification.addConfigurationProperties(['notifier.implementation': 'pushbullet'] as Properties)

        when:
        def result = sendNotification.chooseNotifier()

        then:
        PushbulletNotifier.isInstance(result)
    }
}
