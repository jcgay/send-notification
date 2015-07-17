package fr.jcgay.notification.notifier.additional
import fr.jcgay.notification.DiscoverableNotifier
import fr.jcgay.notification.MultipleSendNotificationException
import fr.jcgay.notification.Notification
import fr.jcgay.notification.SendNotificationException
import fr.jcgay.notification.TestIcon
import org.junit.Rule
import org.junit.contrib.java.lang.system.ClearSystemProperties
import spock.lang.Specification
import spock.lang.Subject

class AdditionalNotifierSpec extends Specification {

    @Rule
    public final ClearSystemProperties clearActivation = new ClearSystemProperties("notifyAll")

    DiscoverableNotifier growl = Mock()
    DiscoverableNotifier pushbullet = Mock()
    DiscoverableNotifier anybar = Mock()

    Set<DiscoverableNotifier> secondary = [pushbullet, anybar] as Set

    @Subject
    AdditionalNotifier notifier

    def "should only init primary notifier when additional notifiers are not activated"() {
        given:
        notifier = new AdditionalNotifier(growl, secondary)

        when:
        notifier.init()

        then:
        1 * growl.init()
        0 * pushbullet._
        0 * anybar._
    }

    def "should init each notifier when additional notifiers are activated"() {
        given:
        secondary_notification_is_activated()

        and:
        notifier = new AdditionalNotifier(growl, secondary)

        when:
        notifier.init()

        then:
        1 * growl.init()
        1 * pushbullet.init()
        1 * anybar.init()
    }

    def "should only close primary notifier when additional notifiers are not activated"() {
        given:
        notifier = new AdditionalNotifier(growl, secondary)

        when:
        notifier.close()

        then:
        1 * growl.close()
        0 * pushbullet._
        0 * anybar._
    }

    def "should close every notifier when additional notifiers are activated"() {
        given:
        secondary_notification_is_activated()

        and:
        notifier = new AdditionalNotifier(growl, secondary)

        when:
        notifier.close()

        then:
        1 * growl.close()
        1 * pushbullet.close()
        1 * anybar.close()
    }

    def "should send notification for each notifier when additional notifiers are activated"() {
        given:
        secondary_notification_is_activated()

        and:
        notifier = new AdditionalNotifier(growl, secondary)

        and:
        Notification notification = anyNotification()

        when:
        notifier.send(notification)

        then:
        1 * growl.send(notification)
        1 * pushbullet.send(notification)
        1 * anybar.send(notification)
    }

    def "should only send notification for primary notifier when additional notifiers are not activated"() {
        given:
        notifier = new AdditionalNotifier(growl, secondary)

        and:
        Notification notification = anyNotification()

        when:
        notifier.send(notification)

        then:
        1 * growl.send(notification)
        0 * pushbullet._
        0 * anybar._
    }

    def "should_collect_errors_when_initialization_fails"() {
        given:
        secondary_notification_is_activated()
        Exception growlError = new RuntimeException('growl')
        Exception anybarError = new SendNotificationException('anybar')

        and:
        notifier = new AdditionalNotifier(growl, secondary)

        when:
        notifier.init()

        then:
        def result = thrown(MultipleSendNotificationException)
        result.message == """growl
                            |anybar""".stripMargin()
        result.errors == [growlError, anybarError]

        1 * growl.init() >> { throw growlError }
        1 * pushbullet.init()
        1 * anybar.init() >> { throw anybarError }
    }

    def "should collect errors when sending notification fails"() {
        given:
        secondary_notification_is_activated()
        Exception growlError = new RuntimeException('growl')
        Exception pushbulletError = new SendNotificationException('pushbullet')

        and:
        notifier = new AdditionalNotifier(growl, secondary)

        when:
        notifier.send(anyNotification())

        then:
        def result = thrown(MultipleSendNotificationException)
        result.message == """growl
                            |pushbullet""".stripMargin()
        result.errors == [growlError, pushbulletError]

        1 * growl.send(_) >> { throw growlError }
        1 * pushbullet.send(_) >> { throw pushbulletError }
        1 * anybar.send(_)
    }

    def "should collect errors when closing notifiers fails"() {
        given:
        secondary_notification_is_activated()
        Exception growlError = new RuntimeException('growl')
        Exception pushbulletError = new SendNotificationException('pushbullet')
        Exception anybarError = new NullPointerException()

        and:
        notifier = new AdditionalNotifier(growl, secondary)

        when:
        notifier.close()

        then:
        def result = thrown(MultipleSendNotificationException)
        result.message == """growl
                            |pushbullet""".stripMargin()
        result.errors == [growlError, pushbulletError, anybarError]

        1 * growl.close() >> { throw growlError }
        1 * pushbullet.close() >> { throw pushbulletError }
        1 * anybar.close() >> { throw anybarError }
    }

    private static Notification anyNotification() {
        Notification.builder('title', 'message', TestIcon.ok()).build()
    }

    private static String secondary_notification_is_activated() {
        System.setProperty("notifyAll", "true")
    }
}
