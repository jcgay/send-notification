package fr.jcgay.notification.notifier.toaster;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.notifier.executor.RuntimeExecutor;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class ToasterProvider implements NotifierProvider {

    @Override
    public String id() {
        return "toaster";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new ToasterNotifier(ToasterConfiguration.create(properties), new RuntimeExecutor(application.timeout()));
    }
}
