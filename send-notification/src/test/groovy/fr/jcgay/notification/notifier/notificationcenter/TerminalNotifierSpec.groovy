package fr.jcgay.notification.notifier.notificationcenter
import fr.jcgay.notification.Application
import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import fr.jcgay.notification.notifier.executor.Executor
import spock.lang.Specification

class TerminalNotifierSpec extends Specification {

    Application application
    Executor result
    TerminalNotifier notifier

    def setup() {
        application = Application.builder('id', 'name', TestIcon.ok()).build()
        result = new Executor() {
            private String[] command

            @Override
            void exec(String[] command) {
                if (command != null) {
                    this.command = command
                }
            }
        }
        notifier = new TerminalNotifier(application, TerminalNotifierConfiguration.byDefault(), result)
    }

    def "should build command line to call terminal-notifier"() {

        setup:
        def notification = Notification.builder('title', 'message', TestIcon.ok()).withSubtitle('subtitle').build()

        when:
        notifier.send(notification)

        then:
        result.command == [
                'terminal-notifier',
                '-title', 'name',
                '-subtitle', 'subtitle',
                '-message', 'message',
                '-group', 'id',
                '-activate', 'com.apple.Terminal',
                '-contentImage', "${System.getProperty('java.io.tmpdir')}send-notifications-icons/ok.png"
        ]
    }

    def "should not set subtitle when notification does not set one"() {

        setup:
        def notification = Notification.builder('title', 'message', TestIcon.ok()).build()

        when:
        notifier.send(notification)

        then:
        !result.command.contains('-subtitle')
    }
}
