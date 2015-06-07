package fr.jcgay.notification.notifier.toaster

import fr.jcgay.notification.Notification
import fr.jcgay.notification.TestIcon
import spock.lang.Specification


class ToasterNotifierSpec extends Specification {

    List<String> executedCommand
    ToasterNotifier notifier = new ToasterNotifier(ToasterConfiguration.byDefault(), { String[] command -> executedCommand = command })
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
}
