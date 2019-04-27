package fr.jcgay.notification.notifier.anybar;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class AnyBarProvider implements NotifierProvider {

    @Override
    public String id() {
        return "anybar";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return AnyBarNotifier.create(application, AnyBarConfiguration.create(properties));
    }
}
