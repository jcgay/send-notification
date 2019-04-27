package fr.jcgay.notification.notifier.pushbullet;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class PushbulletProvider implements NotifierProvider {

    @Override
    public String id() {
        return "pushbullet";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new PushbulletNotifier(application, PushbulletConfiguration.create(properties));
    }
}
