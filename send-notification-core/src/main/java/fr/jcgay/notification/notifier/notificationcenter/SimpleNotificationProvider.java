package fr.jcgay.notification.notifier.notificationcenter;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.notifier.executor.RuntimeExecutor;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class SimpleNotificationProvider implements NotifierProvider {

    @Override
    public String id() {
        return "simplenc";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new SimpleNotificationCenterNotifier(TerminalNotifierConfiguration.create(properties), new RuntimeExecutor(application.timeout()));
    }
}
