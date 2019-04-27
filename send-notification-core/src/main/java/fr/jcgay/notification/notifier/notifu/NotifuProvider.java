package fr.jcgay.notification.notifier.notifu;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.notifier.executor.RuntimeExecutor;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class NotifuProvider implements NotifierProvider {

    @Override
    public String id() {
        return "notifu";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new NotifuNotifier(application, NotifuConfiguration.create(properties), new RuntimeExecutor(application.timeout()));
    }
}
