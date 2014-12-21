package fr.jcgay.notification.notifier.notifysend
import fr.jcgay.notification.Application
import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import fr.jcgay.notification.notifier.executor.Executor
import spock.lang.Specification

class NotifySendNotifierSpec extends Specification {

    Application application
    Executor result
    NotifySendNotifier notifier
    Notification notification

    def setup() {
        application = Application.builder('id', 'name', TestIcon.ok()).withTimeout(3).build()
        result = new Executor() {
            String[] command

            @Override
            void exec(String[] command) {
                if (command != null) {
                    this.command = command
                }
            }
        }
        notifier = new NotifySendNotifier(application, NotifySendConfiguration.byDefault(), result)
        notification = Notification.builder('title', 'message', TestIcon.ok()).build()
    }

    def "should build command line to call notify-send"() {

        when:
        notifier.send(notification)

        then:
        result.command == [
                'notify-send', 'title', 'message',
                '-t', "${application.timeout()}",
                '-i', new File("${System.getProperty('java.io.tmpdir')}/send-notifications-icons/ok.png").path
        ]
    }

    def "should not set timeout when application timeout is equals to -1"() {

        setup:
        def application = Application.builder('id', 'name', TestIcon.ok()).build()
        def notifier = new NotifySendNotifier(application, NotifySendConfiguration.byDefault(), result)

        when:
        notifier.send(notification)

        then:
        !result.command.contains('-t')
    }
}
