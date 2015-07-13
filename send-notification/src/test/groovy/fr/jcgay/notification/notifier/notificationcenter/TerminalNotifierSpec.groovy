package fr.jcgay.notification.notifier.notificationcenter
import fr.jcgay.notification.Application
import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import fr.jcgay.notification.notifier.executor.Executor
import fr.jcgay.notification.notifier.executor.RuntimeExecutor
import spock.lang.Specification
import spock.lang.Subject

class TerminalNotifierSpec extends Specification {

    Application application = Application.builder('id', 'name', TestIcon.application()).build()
    String temp = System.getProperty('java.io.tmpdir')

    List<String> executedCommand
    Executor executor = { String[] command -> executedCommand = command; Stub(Process) }

    @Subject
    TerminalNotifier notifier

    def setup() {
        def configuration = TerminalNotifierConfiguration.create([
            'notifier.notification-center.path':TerminalNotifierConfiguration.byDefault().bin(),
            'notifier.notification-center.activate':TerminalNotifierConfiguration.byDefault().activateApplication(),
            'notifier.notification-center.sound':'default'
        ] as Properties)
        notifier = new TerminalNotifier(application, configuration, executor)
    }

    def "should build command line to call terminal-notifier"() {
        given:
        def notification = Notification.builder('title', 'message', TestIcon.ok())
            .subtitle('subtitle')
            .build()

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
                '-contentImage', new File("${temp}/send-notifications-icons/ok.png").path,
                '-sound', 'default',
                '-appIcon', new File("${temp}/send-notifications-icons/application.png").path
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

    def "should not set sound when configuration does not includes one"() {
        given:
        def notifier = new TerminalNotifier(application, TerminalNotifierConfiguration.byDefault(), executor)
        def notification = Notification.builder('title', 'message', TestIcon.ok()).build()

        when:
        notifier.send(notification)

        then:
        !executedCommand.contains('-sound')
    }

    def "should return true when binary is available"() {
        given:
        RuntimeExecutor executor = Mock()

        and:
        def notifier = new TerminalNotifier(application, TerminalNotifierConfiguration.byDefault(), executor)

        when:
        def result = notifier.tryInit()

        then:
        result
        1 * executor.exec([TerminalNotifierConfiguration.byDefault().bin(), '-help']) >> Stub(Process) { waitFor() >> 0 }
    }

    def "should return false when binary is not available"() {
        given:
        RuntimeExecutor executor = Mock()

        and:
        def notifier = new TerminalNotifier(application, TerminalNotifierConfiguration.byDefault(), executor)

        when:
        def result = notifier.tryInit()

        then:
        !result
        1 * executor.exec([TerminalNotifierConfiguration.byDefault().bin(), '-help']) >> Stub(Process) { waitFor() >> 127 }
    }
}
