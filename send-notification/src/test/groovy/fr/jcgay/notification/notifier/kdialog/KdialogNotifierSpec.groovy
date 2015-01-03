package fr.jcgay.notification.notifier.kdialog
import fr.jcgay.notification.Application
import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import fr.jcgay.notification.notifier.executor.Executor
import spock.lang.Specification

import java.util.concurrent.TimeUnit

class KdialogNotifierSpec extends Specification {

    Application application
    Executor result
    KdialogNotifier notifier
    Notification notification

    def setup() {
        application = Application.builder('id', 'name', TestIcon.ok()).withTimeout(TimeUnit.SECONDS.toMillis(3)).build()
        result = new Executor() {
            String[] command

            @Override
            void exec(String[] command) {
                if (command != null) {
                    this.command = command
                }
            }
        }
        notifier = new KdialogNotifier(application, KdialogConfiguration.byDefault(), result)
        notification = Notification.builder('title', 'message', TestIcon.ok()).build()
    }

    def "should build command line to call kdialog"() {

        when:
        notifier.send(notification)

        then:
        result.command == [
                'kdialog',
                '--passivepopup', notification.message(), '3',
                '--title', notification.title(),
                '--icon', notification.icon().asPath()
        ]
    }

    def "should not set timeout when application timeout is the default one"() {

        setup:
        application = Application.builder('id', 'name', TestIcon.ok()).build()
        notifier = new KdialogNotifier(application, KdialogConfiguration.byDefault(), result)

        when:
        notifier.send(notification)

        then:
        result.command == [
                'kdialog',
                '--passivepopup', notification.message(),
                '--title', notification.title(),
                '--icon', notification.icon().asPath()
        ]
    }

}
