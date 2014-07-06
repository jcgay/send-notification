package fr.jcgay.notification;

import fr.jcgay.notification.configuration.ConfigurationReader;
import fr.jcgay.notification.notifier.executor.RuntimeExecutor;
import fr.jcgay.notification.notifier.growl.GrowlConfiguration;
import fr.jcgay.notification.notifier.growl.GrowlNotifier;
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifier;
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifierConfiguration;

import java.util.Properties;

public class SendNotification {

    private ConfigurationReader configuration;
    private Application application;
    private String chosenNotifier;

    SendNotification(ConfigurationReader configuration) {
        this.configuration = configuration;
    }

    public SendNotification() {
        this(ConfigurationReader.atPath(System.getProperty("user.home") + "/.send-notification"));
    }

    public Notifier chooseNotifier() {
        Properties properties = configuration.get();

        if ("growl".equalsIgnoreCase(chosenNotifier)) {
            return new GrowlNotifier(application, GrowlConfiguration.create(properties));
        } else if ("notificationcenter".equals(chosenNotifier)) {
            return new TerminalNotifier(application, TerminalNotifierConfiguration.create(properties), new RuntimeExecutor());
        }
        return null;
    }

    public SendNotification setApplication(Application application) {
        this.application = application;
        return this;
    }

    public SendNotification setChosenNotifier(String chosenNotifier) {
        this.chosenNotifier = chosenNotifier;
        return this;
    }

    public SendNotification setConfigurationPath(String configurationPath) {
        this.configuration = ConfigurationReader.atPath(configurationPath);
        return this;
    }
}
