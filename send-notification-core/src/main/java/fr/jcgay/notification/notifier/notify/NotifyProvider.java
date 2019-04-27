package fr.jcgay.notification.notifier.notify;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class NotifyProvider implements NotifierProvider {

    @Override
    public String id() {
        return "notify";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new NotifyNotifier(application, NotifyConfiguration.create(properties));
    }
}
