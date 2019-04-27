package fr.jcgay.notification.notifier.notifysend;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.notifier.executor.RuntimeExecutor;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class NotifySendProvider implements NotifierProvider {

    @Override
    public String id() {
        return "notifysend";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new NotifySendNotifier(application, NotifySendConfiguration.create(properties), new RuntimeExecutor(application.timeout()));
    }
}
