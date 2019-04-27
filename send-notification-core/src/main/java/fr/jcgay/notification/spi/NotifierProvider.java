package fr.jcgay.notification.spi;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;

import java.util.Properties;

public interface NotifierProvider {

    String id();

    DiscoverableNotifier create(Application application, Properties properties);

    default DiscoverableNotifier createQuietly(Application application, Properties properties) {
        return create(application, properties);
    }
}
