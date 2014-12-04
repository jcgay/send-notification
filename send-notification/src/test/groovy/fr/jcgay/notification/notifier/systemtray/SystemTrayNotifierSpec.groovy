package fr.jcgay.notification.notifier.systemtray

import fr.jcgay.notification.Application
import fr.jcgay.notification.TestIcon
import spock.lang.Specification

class SystemTrayNotifierSpec extends Specification {

    def "should not fail when application has default timeout"() {

        setup:
        def application = Application.builder('id', 'name', TestIcon.ok()).build()
        def notifier = new SystemTrayNotifier(application)

        when:
        notifier.close()

        then:
        notThrown(IllegalArgumentException)
    }
}
