package fr.jcgay.notification.notifier.systemtray

import fr.jcgay.notification.Application
import fr.jcgay.notification.TestIcon
import spock.lang.IgnoreIf
import spock.lang.Specification

import java.awt.SystemTray

class SystemTrayNotifierSpec extends Specification {

    @IgnoreIf({ !SystemTray.isSupported() })
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
