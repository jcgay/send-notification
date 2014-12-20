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
import org.slf4j.Logger;

import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Fluent builder to create a {@link fr.jcgay.notification.Notifier}. <br />
 *
 * You'll need a configuration file (by default: {@code {user.home}/.send-notification}
 * to select and configure a {@link fr.jcgay.notification.Notifier}. <br />
 * This file must be property based (example):
 * <pre>{@code
 * notifier.implementation = pushbullet
 * notifier.message.short = true
 * notifier.pushbullet.apikey = 1234
 * }</pre>
 *
 * Then to create a notifier you can use:
 * <pre>{@code
 * Notifier notifier = new SendNotification()
 *  .setApplication(application)
 *  .chooseNotifier();
 * }</pre>
 */
public class SendNotification {

    private static final Logger LOGGER = getLogger(SendNotification.class);

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

    /**
     * Build a {@link fr.jcgay.notification.Notifier}.
     *
     * @return notifier based on provided configuration.
     */
    public Notifier chooseNotifier() {
        Properties properties = configuration.get();
        LOGGER.debug("Configuration is: {}.", properties);
        if (additionalConfiguration != null) {
            LOGGER.debug("Overriding previous configuration with: {}.", additionalConfiguration);
            properties.putAll(additionalConfiguration);
        }

        if (chosenNotifier == null) {
            chosenNotifier = (String) properties.get("notifier.implementation");
        }

        LOGGER.debug("Notifications will be send to: {} for application: {}.", chosenNotifier, application);
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

    /**
     * Define application.
     *
     * @param application {@link fr.jcgay.notification.Application} we want to send notification from.
     *
     * @return fluent builder.
     */
    public SendNotification setApplication(Application application) {
        this.application = application;
        return this;
    }

    /**
     * Choose a {@link fr.jcgay.notification.Notifier} implementation when you don't want to
     * rely on property file configuration.
     *
     * @param chosenNotifier a notifier name to instantiate.
     *
     * @return fluent builder.
     */
    public SendNotification setChosenNotifier(String chosenNotifier) {
        this.chosenNotifier = chosenNotifier;
        return this;
    }

    /**
     * Change the path where the configuration will be read from. <br />
     * Default one is: {@code {user.home}/.send-notification}
     *
     * @param configurationPath a file path for configuration file.
     *
     * @return fluent builder.
     */
    public SendNotification setConfigurationPath(String configurationPath) {
        this.configuration = ConfigurationReader.atPath(configurationPath);
        return this;
    }

    /**
     * Add properties that will override existing ones (read from the configuration file).
     *
     * @param additionalConfiguration configuration (key/value) as {@link java.util.Properties}.
     *
     * @return fluent builder.
     */
    public SendNotification addConfigurationProperties(Properties additionalConfiguration) {
        this.additionalConfiguration = additionalConfiguration;
        return this;
    }
}
