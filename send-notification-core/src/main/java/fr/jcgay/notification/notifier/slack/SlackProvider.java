package fr.jcgay.notification.notifier.slack;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class SlackProvider implements NotifierProvider {

    @Override
    public String id() {
        return "slack";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new SlackNotifier(application, SlackConfiguration.create(properties));
    }
}
