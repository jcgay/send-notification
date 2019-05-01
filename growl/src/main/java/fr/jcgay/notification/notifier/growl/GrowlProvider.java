package fr.jcgay.notification.notifier.growl;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class GrowlProvider implements NotifierProvider {

    @Override
    public String id() {
        return "growl";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new GrowlNotifier(application, GrowlConfiguration.create(properties), GntpSlf4jListener.ERROR);
    }

    @Override
    public DiscoverableNotifier createQuietly(Application application, Properties properties) {
        return new GrowlNotifier(application, GrowlConfiguration.create(properties), GntpSlf4jListener.DEBUG);
    }
}
