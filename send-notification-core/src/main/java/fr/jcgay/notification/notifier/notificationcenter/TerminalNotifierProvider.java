package fr.jcgay.notification.notifier.notificationcenter;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.notifier.executor.RuntimeExecutor;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class TerminalNotifierProvider implements NotifierProvider {

    @Override
    public String id() {
        return "notificationcenter";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new TerminalNotifier(application, TerminalNotifierConfiguration.create(properties), new RuntimeExecutor(application.timeout()));
    }
}
