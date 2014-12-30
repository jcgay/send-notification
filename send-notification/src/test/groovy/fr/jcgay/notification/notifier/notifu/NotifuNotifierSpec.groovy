package fr.jcgay.notification.notifier.notifu
import fr.jcgay.notification.Application
import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import fr.jcgay.notification.notifier.executor.Executor
import spock.lang.Specification

class NotifuNotifierSpec extends Specification {

    Application application
    Executor result
    NotifuNotifier notifier
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
        notifier = new NotifuNotifier(application, NotifuConfiguration.byDefault(), result)
        notification = Notification.builder('title', 'message', TestIcon.ok()).build()
    }

    def "should build command line to call notifu"() {

        when:
        notifier.send(notification)

        then:
        result.command == [
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
                .withLevel(level)
                .build()

        when:
        notifier.send(notification)

        then:
        result.command.contains(urgency)

        where:
        level                      | urgency
        Notification.Level.INFO    | 'info'
        Notification.Level.WARNING | 'warn'
        Notification.Level.ERROR   | 'error'
    }
}
