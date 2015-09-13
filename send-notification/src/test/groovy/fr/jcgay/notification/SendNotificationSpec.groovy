package fr.jcgay.notification
import fr.jcgay.notification.configuration.ConfigurationReader
import fr.jcgay.notification.configuration.OperatingSystem
import fr.jcgay.notification.notifier.DoNothingNotifier
import fr.jcgay.notification.notifier.additional.AdditionalNotifier
import fr.jcgay.notification.notifier.anybar.AnyBarNotifier
import fr.jcgay.notification.notifier.growl.GrowlNotifier
import fr.jcgay.notification.notifier.kdialog.KdialogNotifier
import fr.jcgay.notification.notifier.notificationcenter.SimpleNotificationCenterNotifier
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifier
import fr.jcgay.notification.notifier.notifu.NotifuNotifier
import fr.jcgay.notification.notifier.notifysend.NotifySendNotifier
import fr.jcgay.notification.notifier.pushbullet.PushbulletNotifier
import fr.jcgay.notification.notifier.snarl.SnarlNotifier
import fr.jcgay.notification.notifier.systemtray.SystemTrayNotifier
import fr.jcgay.notification.notifier.toaster.ToasterNotifier
import spock.lang.Specification
import spock.lang.Unroll

class SendNotificationSpec extends Specification {

    SendNotification sendNotification
    Properties properties = ['notifier.pushbullet.apikey': 'apikey']
    Application defaultApplication = Application.builder("id", "name", TestIcon.application()).build()

    void setup() {
        sendNotification = new SendNotification(new ConfigurationReader(properties), new OperatingSystem())
    }

    @Unroll
    def "should return notifier [#implementation.simpleName] when user chooses #notifier"() {
        given:
        properties << ['notifier.implementation': notifier]

        when:
        def result = sendNotification
                .setApplication(defaultApplication)
                .chooseNotifier()

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
        'simplenc'           || SimpleNotificationCenterNotifier
        'toaster'            || ToasterNotifier
        'growl,pushbullet'   || AdditionalNotifier
        'unknown'            || DoNothingNotifier
        'none'               || DoNothingNotifier
    }

    @Unroll
    def "should return notifier [#implementation.simpleName] when user setting is override by #notifier"() {
        when:
        def result = sendNotification
                .setApplication(defaultApplication)
                .setChosenNotifier(notifier).chooseNotifier()

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
        'simplenc'           || SimpleNotificationCenterNotifier
        'toaster'            || ToasterNotifier
        'growl,pushbullet'   || AdditionalNotifier
        'unknown'            || DoNothingNotifier
        'none'               || DoNothingNotifier
    }

    def "should return available notifier when no configuration is present"() {
        given:
        DiscoverableNotifier notAvailable = Stub() {
            tryInit() >> false
        }
        DiscoverableNotifier isAvailable = Stub() {
            tryInit() >> true
        }

        NotifierProvider provider = Stub() {
            available(_, _) >> [notAvailable, isAvailable]
        }

        and:
        sendNotification = new SendNotification(new ConfigurationReader(properties), provider)

        when:
        def result = sendNotification.chooseNotifier()

        then:
        result == isAvailable
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
