package fr.jcgay.notification;

public class SendNotification {

    private Application application;
    private String choosenNotifier;
    private String configurationPath;

    SendNotification() {

        this(ConfigurationReader.atPath(System.getProperty("user.home") + "/.send-notification"));
    }

    public Notifier chooseNotifier() {
        return null;
    }

    SendNotification setApplication(Application application) {
        this.application = application;
        return this;
    }

    SendNotification setChoosenNotifier(String choosenNotifier) {
        this.choosenNotifier = choosenNotifier;
        return this;
    }

    SendNotification setConfigurationPath(String configurationPath) {
        this.configurationPath = configurationPath;
        return this;
    }
}
