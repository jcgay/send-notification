package fr.jcgay.notification.notifier.notifysend
import fr.jcgay.notification.Application
import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import fr.jcgay.notification.notifier.executor.Executor
import fr.jcgay.notification.notifier.executor.RuntimeExecutor
import spock.lang.Specification
import spock.lang.Subject

import static fr.jcgay.notification.Notification.Level.*

class NotifySendNotifierSpec extends Specification {

    Application application
    Notification notification

    Executor executor = { String[] command -> executedCommand = command; Stub(Process) }
    List<String> executedCommand

    @Subject
    NotifySendNotifier notifier

    def setup() {
        application = Application.builder('id', 'name', TestIcon.ok()).timeout(3).build()
        notification = Notification.builder('title', 'message', TestIcon.ok()).build()
        notifier = new NotifySendNotifier(application, NotifySendConfiguration.byDefault(), executor)
    }

    def "should build command line to call notify-send"() {
        when:
        notifier.send(notification)

        then:
        executedCommand == [
                'notify-send', 'title', 'message',
                '-t', "${application.timeout()}",
                '-i', new File("${System.getProperty('java.io.tmpdir')}/send-notifications-icons/ok.png").path,
                '-u', 'normal'
        ]
    }

    def "should not set timeout when application timeout is equals to -1"() {
        given:
        def application = Application.builder('id', 'name', TestIcon.ok()).build()
        def notifier = new NotifySendNotifier(application, NotifySendConfiguration.byDefault(), executor)

        when:
        notifier.send(notification)

        then:
        !executedCommand.contains('-t')
    }

    def "should translate notification level to urgency"() {
        given:
        def notification = Notification.builder('title', 'message', TestIcon.ok())
                .level(level)
                .build()

        when:
        notifier.send(notification)

        then:
        executedCommand.contains(urgency)

        where:
        level   | urgency
        INFO    | 'normal'
        WARNING | 'critical'
        ERROR   | 'critical'
    }

    def "should return false when binary is not available"() {
        given:
        RuntimeExecutor executor = Mock()

        and:
        def notifier = new NotifySendNotifier(application, NotifySendConfiguration.byDefault(), executor)

        when:
        def result = notifier.tryInit()

        then:
        !result
        1 * executor.exec([NotifySendConfiguration.byDefault().bin(), '-v']) >> Stub(Process) { waitFor() >> 127 }
    }

    def "should return true when binary is available"() {
        given:
        RuntimeExecutor executor = Mock()

        and:
        def notifier = new NotifySendNotifier(application, NotifySendConfiguration.byDefault(), executor)

        when:
        def result = notifier.tryInit()

        then:
        result
        1 * executor.exec([NotifySendConfiguration.byDefault().bin(), '-v']) >> Stub(Process) { waitFor() >> 0 }
    }

}
