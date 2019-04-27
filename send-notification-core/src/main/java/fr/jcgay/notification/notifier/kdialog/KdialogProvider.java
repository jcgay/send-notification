package fr.jcgay.notification.notifier.kdialog;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.notifier.executor.RuntimeExecutor;
import fr.jcgay.notification.spi.NotifierProvider;

import java.util.Properties;

public class KdialogProvider implements NotifierProvider {

    @Override
    public String id() {
        return "kdialog";
    }

    @Override
    public DiscoverableNotifier create(Application application, Properties properties) {
        return new KdialogNotifier(application, KdialogConfiguration.create(properties), new RuntimeExecutor(application.timeout()));
    }
}
