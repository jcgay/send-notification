package fr.jcgay.notification.notifier.toaster

import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import fr.jcgay.notification.notifier.executor.RuntimeExecutor
import spock.lang.Specification


class ToasterNotifierSpec extends Specification {

    List<String> executedCommand
    ToasterNotifier notifier = new ToasterNotifier(ToasterConfiguration.byDefault(), { String[] command -> executedCommand = command; Stub(Process) })
    Notification notification = Notification.builder('title', 'message', TestIcon.ok()).build()

    def "should build command line to call notify-send"() {
        when:
        notifier.send(notification)

        then:
        executedCommand == [
            ToasterConfiguration.byDefault().bin(),
            '-t',
            /"${notification.title()}"/,
            '-m',
            /"${notification.message()}"/,
            '-p',
            /"${notification.icon().asPath()}"/
        ]
    }

    def "should return false when binary is not available"() {
        given:
        RuntimeExecutor executor = Mock()

        and:
        def notifier = new ToasterNotifier(ToasterConfiguration.byDefault(), executor)

        when:
        def result = notifier.tryInit()

        then:
        !result
        1 * executor.exec([ToasterConfiguration.byDefault().bin()]) >> Stub(Process) { waitFor() >> 127 }
    }

    def "should return true when binary is not available"() {
        given:
        RuntimeExecutor executor = Mock()

        and:
        def notifier = new ToasterNotifier(ToasterConfiguration.byDefault(), executor)

        when:
        def result = notifier.tryInit()

        then:
        result
        1 * executor.exec([ToasterConfiguration.byDefault().bin()]) >> Stub(Process) { waitFor() >> 0 }
    }
}
