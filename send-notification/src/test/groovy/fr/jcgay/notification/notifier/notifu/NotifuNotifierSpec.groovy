package fr.jcgay.notification.notifier.notifu

import fr.jcgay.notification.Application
import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import fr.jcgay.notification.notifier.executor.Executor
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
        notifier = new NotifuNotifier(application, NotifuConfiguration.byDefault(), new Executor() {
            @Override
            Process exec(String[] command) {
                executedCommand = command
                null
            }

            @Override
            boolean tryExec(String[] command) {
                throw new IllegalStateException("This method should not be called!")
            }
        })
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

    def "should escape arguments"() {
        given:
        def notification = Notification.builder('ti"tle', 'mes"sage', TestIcon.ok()).build()

        when:
        notifier.send(notification)

        then:
        executedCommand.contains('ti\\"tle')
        executedCommand.contains('mes\\"sage')
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
        1 * executor.tryExec([NotifuConfiguration.byDefault().bin(), "/v"]) >> true
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
        1 * executor.tryExec([NotifuConfiguration.byDefault().bin(), "/v"]) >> false
    }

}
