package fr.jcgay.notification.notifier.kdialog
import fr.jcgay.notification.Application
import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import fr.jcgay.notification.notifier.executor.Executor
import fr.jcgay.notification.notifier.executor.RuntimeExecutor
import spock.lang.Specification

import static java.util.concurrent.TimeUnit.SECONDS

class KdialogNotifierSpec extends Specification {

    Notification notification = Notification.builder('title', 'message', TestIcon.ok()).build()
    Application application = Application.builder('id', 'name', TestIcon.ok()).build()

    List<String> executedCommand
    Executor executor = { String[] command -> executedCommand = command; Stub(Process) }

    def "should build command line to call kdialog"() {
        given:
        def application = Application.builder('id', 'name', TestIcon.ok()).timeout(SECONDS.toMillis(3)).build()
        def notifier = new KdialogNotifier(application, KdialogConfiguration.byDefault(), executor)

        when:
        notifier.send(notification)

        then:
        executedCommand == [
                'kdialog',
                '--passivepopup', notification.message(), '3',
                '--title', notification.title(),
                '--icon', notification.icon().asPath()
        ]
    }

    def "should not set timeout when application timeout is the default one"() {
        given:
        def notifier = new KdialogNotifier(application, KdialogConfiguration.byDefault(), executor)

        when:
        notifier.send(notification)

        then:
        executedCommand == [
                'kdialog',
                '--passivepopup', notification.message(),
                '--title', notification.title(),
                '--icon', notification.icon().asPath()
        ]
    }

    def "should return true if binary is available"() {
        given:
        RuntimeExecutor executor = Mock()

        and:
        def notifier = new KdialogNotifier(application, KdialogConfiguration.byDefault(), executor)

        when:
        def result = notifier.tryInit()

        then:
        result
        1 * executor.exec([KdialogConfiguration.byDefault().bin(), '-v']) >> Stub(Process) { waitFor() >> 0 }
    }

    def "should return false if binary is not available"() {
        given:
        RuntimeExecutor executor = Mock()

        and:
        def notifier = new KdialogNotifier(application, KdialogConfiguration.byDefault(), executor)

        when:
        def result = notifier.tryInit()

        then:
        !result
        1 * executor.exec([KdialogConfiguration.byDefault().bin(), '-v']) >> Stub(Process) { waitFor() >> 127 }
    }
}
