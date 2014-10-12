package fr.jcgay.notification;

import fr.jcgay.notification.configuration.ConfigurationReader;
import fr.jcgay.notification.notifier.DoNothingNotifier;
import fr.jcgay.notification.notifier.executor.RuntimeExecutor;
import fr.jcgay.notification.notifier.growl.GrowlConfiguration;
import fr.jcgay.notification.notifier.growl.GrowlNotifier;
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifier;
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifierConfiguration;
import fr.jcgay.notification.notifier.notifysend.NotifySendConfiguration;
import fr.jcgay.notification.notifier.notifysend.NotifySendNotifier;
import fr.jcgay.notification.notifier.pushbullet.PushbulletConfiguration;
import fr.jcgay.notification.notifier.pushbullet.PushbulletNotifier;
import fr.jcgay.notification.notifier.snarl.SnarlConfiguration;
import fr.jcgay.notification.notifier.snarl.SnarlNotifier;
import fr.jcgay.notification.notifier.systemtray.SystemTrayNotifier;

import java.util.Properties;

public class SendNotification {

    private ConfigurationReader configuration;
    private Application application;
    private String chosenNotifier;
    private Properties additionalConfiguration;

    SendNotification(ConfigurationReader configuration) {
        this.configuration = configuration;
    }

    public SendNotification() {
        this(ConfigurationReader.atPath(System.getProperty("user.home") + "/.send-notification"));
    }

    public Notifier chooseNotifier() {
        Properties properties = configuration.get();
        if (additionalConfiguration != null) {
            properties.putAll(additionalConfiguration);
        }

        if (chosenNotifier == null) {
            chosenNotifier = (String) properties.get("notifier.implementation");
        }

        if ("growl".equalsIgnoreCase(chosenNotifier)) {
            return new GrowlNotifier(application, GrowlConfiguration.create(properties));
        } else if ("notificationcenter".equals(chosenNotifier)) {
            return new TerminalNotifier(application, TerminalNotifierConfiguration.create(properties), new RuntimeExecutor());
        } else if ("notifysend".equals(chosenNotifier)) {
            return new NotifySendNotifier(application, NotifySendConfiguration.create(properties), new RuntimeExecutor());
        } else if ("pushbullet".equals(chosenNotifier)) {
            return new PushbulletNotifier(application, PushbulletConfiguration.create(properties));
        } else if ("snarl".equals(chosenNotifier)) {
            return new SnarlNotifier(application, SnarlConfiguration.create(properties));
        } else if ("systemtray".equals(chosenNotifier)) {
            return new SystemTrayNotifier(application);
        }

        return DoNothingNotifier.doNothing();
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

    public SendNotification addConfigurationProperties(Properties additionalConfiguration) {
        this.additionalConfiguration = additionalConfiguration;
        return this;
    }
}
