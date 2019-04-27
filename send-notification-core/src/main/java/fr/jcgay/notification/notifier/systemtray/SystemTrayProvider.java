package fr.jcgay.notification.notifier.systemtray;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class SystemTrayProvider implements NotifierProvider {

    @Override
    public String id() {
        return "systemtray";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new SystemTrayNotifier(application);
    }
}
