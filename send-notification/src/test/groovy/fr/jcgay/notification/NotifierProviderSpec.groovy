package fr.jcgay.notification

import fr.jcgay.notification.configuration.ChosenNotifiers
import fr.jcgay.notification.configuration.OperatingSystem
import fr.jcgay.notification.notifier.burnttoast.BurntToastNotifier
import fr.jcgay.notification.notifier.burnttoast.BurntToastNotifierConfiguration
import fr.jcgay.notification.notifier.executor.RuntimeExecutor
import fr.jcgay.notification.notifier.growl.GrowlConfiguration
import fr.jcgay.notification.notifier.growl.GrowlNotifier
import fr.jcgay.notification.notifier.kdialog.KdialogConfiguration
import fr.jcgay.notification.notifier.kdialog.KdialogNotifier
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifier
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifierConfiguration
import fr.jcgay.notification.notifier.notifysend.NotifySendConfiguration
import fr.jcgay.notification.notifier.notifysend.NotifySendNotifier
import fr.jcgay.notification.notifier.snarl.SnarlConfiguration
import fr.jcgay.notification.notifier.snarl.SnarlNotifier
import fr.jcgay.notification.notifier.systemtray.SystemTrayNotifier
import fr.jcgay.notification.notifier.toaster.ToasterConfiguration
import fr.jcgay.notification.notifier.toaster.ToasterNotifier
import spock.lang.Specification

import static fr.jcgay.notification.notifier.growl.GntpSlf4jListener.DEBUG
import static fr.jcgay.notification.notifier.growl.GntpSlf4jListener.ERROR
import static org.assertj.core.api.Assertions.assertThat

class NotifierProviderSpec extends Specification {

    Application application = Application.builder('id', 'name', TestIcon.application()).build()

    def "should return all available notifiers for mac"() {
        given:
        def provider = new NotifierProvider(new OperatingSystem('mac'))

        when:
        def result = provider.available([:] as Properties, application)

        then:
        assertThat(result).containsExactly(
            new GrowlNotifier(application, GrowlConfiguration.byDefault(), DEBUG),
            new TerminalNotifier(application, TerminalNotifierConfiguration.byDefault(), new RuntimeExecutor(application.timeout())),
            new SystemTrayNotifier(application)
        )
    }

    def "should return all available notifiers for windows"() {
        given:
        def provider = new NotifierProvider(new OperatingSystem('windows'))

        when:
        def result = provider.available([:] as Properties, application)

        then:
        assertThat(result).containsExactly(
            new SnarlNotifier(application, SnarlConfiguration.byDefault()),
            new GrowlNotifier(application, GrowlConfiguration.byDefault(), DEBUG),
            new ToasterNotifier(ToasterConfiguration.byDefault(), new RuntimeExecutor(application.timeout())),
            new BurntToastNotifier(application, BurntToastNotifierConfiguration.byDefault()),
            new SystemTrayNotifier(application)
        )
    }

    def "should return all available notifiers for linux"() {
        given:
        def provider = new NotifierProvider(new OperatingSystem('linux'))

        when:
        def result = provider.available([:] as Properties, application)

        then:
        assertThat(result).containsExactly(
            new KdialogNotifier(application, KdialogConfiguration.byDefault(), new RuntimeExecutor(application.timeout())),
            new NotifySendNotifier(application, NotifySendConfiguration.byDefault(), new RuntimeExecutor(application.timeout())),
            new SystemTrayNotifier(application)
        )
    }

    def "should return growl notifier with log level error"() {
        given:
        def provider = new NotifierProvider(new OperatingSystem('mac'))

        when:
        def result = provider.byName(ChosenNotifiers.from('growl'), [:] as Properties, application)

        then:
        result == new GrowlNotifier(application, GrowlConfiguration.byDefault(), ERROR)
    }
}
