package fr.jcgay.notification

import fr.jcgay.notification.configuration.ConfigurationReader
import fr.jcgay.notification.configuration.OperatingSystem
import fr.jcgay.notification.notifier.DoNothingNotifier
import fr.jcgay.notification.notifier.anybar.AnyBarNotifier
import fr.jcgay.notification.notifier.growl.GrowlNotifier
import fr.jcgay.notification.notifier.kdialog.KdialogNotifier
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifier
import fr.jcgay.notification.notifier.notifu.NotifuNotifier
import fr.jcgay.notification.notifier.notifysend.NotifySendNotifier
import fr.jcgay.notification.notifier.pushbullet.PushbulletNotifier
import fr.jcgay.notification.notifier.snarl.SnarlNotifier
import fr.jcgay.notification.notifier.systemtray.SystemTrayNotifier
import spock.lang.Specification
import spock.lang.Unroll

class SendNotificationSpec extends Specification {

    SendNotification sendNotification
    Properties properties = ['notifier.pushbullet.apikey': 'apikey']

    void setup() {
        sendNotification = new SendNotification(new ConfigurationReader(properties), new OperatingSystem())
    }

    @Unroll
    def "should return notifier [#implementation] when user chooses #notifier"() {
        given:
        properties << ['notifier.implementation': notifier]

        when:
        def result = sendNotification.chooseNotifier()

        then:
        implementation.isInstance(result)

        where:
        notifier             || implementation
        'growl'              || GrowlNotifier
        'notificationcenter' || TerminalNotifier
        'notifysend'         || NotifySendNotifier
        'pushbullet'         || PushbulletNotifier
        'snarl'              || SnarlNotifier
        'systemtray'         || SystemTrayNotifier
        'notifu'             || NotifuNotifier
        'kdialog'            || KdialogNotifier
        'anybar'             || AnyBarNotifier
        'unknown'            || DoNothingNotifier
    }

    @Unroll
    def "should return notifier [#implementation] when user setting is override by #notifier"() {
        when:
        def result = sendNotification.setChosenNotifier(notifier).chooseNotifier()

        then:
        implementation.isInstance(result)

        where:
        notifier             || implementation
        'growl'              || GrowlNotifier
        'notificationcenter' || TerminalNotifier
        'notifysend'         || NotifySendNotifier
        'pushbullet'         || PushbulletNotifier
        'snarl'              || SnarlNotifier
        'systemtray'         || SystemTrayNotifier
        'notifu'             || NotifuNotifier
        'kdialog'            || KdialogNotifier
        'anybar'             || AnyBarNotifier
        'unknown'            || DoNothingNotifier
    }

    @Unroll
    def "should return default notifier [#implementation] for #currentOs when no configuration (file or manually set) is present"() {
        given:
        sendNotification = new SendNotification(new ConfigurationReader(properties), new OperatingSystem(currentOs))

        when:
        def result = sendNotification.chooseNotifier()

        then:
        implementation.isInstance(result)

        where:
        currentOs    || implementation
        'Mac OS X'   || GrowlNotifier
        'Windows XP' || GrowlNotifier
        'Linux'      || NotifySendNotifier
    }

    def "should override configuration when adding properties"() {
        given:
        properties << ['notifier.implementation': 'growl']
        sendNotification.addConfigurationProperties(['notifier.implementation': 'pushbullet'] as Properties)

        when:
        def result = sendNotification.chooseNotifier()

        then:
        PushbulletNotifier.isInstance(result)
    }
}
