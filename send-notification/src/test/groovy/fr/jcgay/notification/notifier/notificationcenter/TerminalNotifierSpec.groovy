package fr.jcgay.notification.notifier.notificationcenter
import fr.jcgay.notification.Application
import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import spock.lang.Specification
import spock.lang.Subject

class TerminalNotifierSpec extends Specification {

    Application application = Application.builder('id', 'name', TestIcon.ok()).build()
    List<String> executedCommand

    @Subject
    TerminalNotifier notifier

    def setup() {
        notifier = new TerminalNotifier(application, TerminalNotifierConfiguration.byDefault(), { String[] command -> executedCommand = command })
    }

    def "should build command line to call terminal-notifier"() {
        given:
        def notification = Notification.builder('title', 'message', TestIcon.ok()).subtitle('subtitle').build()

        when:
        notifier.send(notification)

        then:
        executedCommand == [
                'terminal-notifier',
                '-title', 'name',
                '-subtitle', 'subtitle',
                '-message', 'message',
                '-group', 'id',
                '-activate', 'com.apple.Terminal',
                '-contentImage', new File("${System.getProperty('java.io.tmpdir')}/send-notifications-icons/ok.png").path
        ]
    }

    def "should not set subtitle when notification does not set one"() {
        given:
        def notification = Notification.builder('title', 'message', TestIcon.ok()).build()

        when:
        notifier.send(notification)

        then:
        !executedCommand.contains('-subtitle')
    }
}
