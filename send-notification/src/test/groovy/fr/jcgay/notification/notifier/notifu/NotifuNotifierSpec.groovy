package fr.jcgay.notification.notifier.notifu
import fr.jcgay.notification.Application
import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import fr.jcgay.notification.notifier.executor.RuntimeExecutor
import spock.lang.Specification
import spock.lang.Subject

import static fr.jcgay.notification.Notification.Level.ERROR
import static fr.jcgay.notification.Notification.Level.INFO
import static fr.jcgay.notification.Notification.Level.WARNING

class NotifuNotifierSpec extends Specification {

    Application application = Application.builder('id', 'name', TestIcon.ok()).timeout(3).build()

    List<String> executedCommand

    @Subject
    NotifuNotifier notifier

    def setup() {
        notifier = new NotifuNotifier(application, NotifuConfiguration.byDefault(), { String[] command -> executedCommand = command; Stub(Process) })
    }

    def "should build command line to call notifu"() {
        given:
        def notification = Notification.builder('title', 'message', TestIcon.ok()).build()

        when:
        notifier.send(notification)

        then:
        executedCommand == [
                'notifu64',
                '/p', notification.title(),
                '/m', notification.message(),
                '/d', '3',
                '/t', 'info',
                '/q'
        ]
    }

    def "should translate notification level to type"() {
        given:
        def notification = Notification.builder('title', 'message', TestIcon.ok())
                .level(level)
                .build()

        when:
        notifier.send(notification)

        then:
        executedCommand.contains(urgency)

        where:
        level   || urgency
        INFO    || 'info'
        WARNING || 'warn'
        ERROR   || 'error'
    }

    def "should return true when binary is available"() {
        given:
        RuntimeExecutor executor = Mock()

        and:
        notifier = new NotifuNotifier(application, NotifuConfiguration.byDefault(), executor)

        when:
        def result = notifier.tryInit()

        then:
        result
        1 * executor.exec([NotifuConfiguration.byDefault().bin(), "/v"]) >> Stub(Process) { waitFor() >> 0 }
    }

    def "should return false when binary is available"() {
        given:
        RuntimeExecutor executor = Mock()

        and:
        notifier = new NotifuNotifier(application, NotifuConfiguration.byDefault(), executor)

        when:
        def result = notifier.tryInit()

        then:
        !result
        1 * executor.exec([NotifuConfiguration.byDefault().bin(), "/v"]) >> Stub(Process) { waitFor() >> 127 }
    }

}
