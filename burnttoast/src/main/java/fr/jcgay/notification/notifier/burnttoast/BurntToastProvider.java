package fr.jcgay.notification.notifier.burnttoast;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class BurntToastProvider implements NotifierProvider {

    @Override
    public String id() {
        return "burnttoast";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new BurntToastNotifier(application, BurntToastNotifierConfiguration.create(properties));
    }
}
