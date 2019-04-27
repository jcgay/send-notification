package fr.jcgay.notification.notifier.snarl;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class SnarlProvider implements NotifierProvider {

    @Override
    public String id() {
        return "snarl";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new SnarlNotifier(application, SnarlConfiguration.create(properties));
    }
}
