package fr.jcgay.notification;

import fr.jcgay.notification.configuration.ConfigurationReader;

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
        return null;
    }

    SendNotification setApplication(Application application) {
        this.application = application;
        return this;
    }

    SendNotification setChosenNotifier(String chosenNotifier) {
        this.chosenNotifier = chosenNotifier;
        return this;
    }

    SendNotification setConfigurationPath(String configurationPath) {
        this.configuration = ConfigurationReader.atPath(configurationPath);
        return this;
    }
}
