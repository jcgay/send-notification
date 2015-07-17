package fr.jcgay.notification;

import com.google.common.annotations.VisibleForTesting;
import fr.jcgay.notification.configuration.ChosenNotifiers;
import fr.jcgay.notification.configuration.ConfigurationReader;
import fr.jcgay.notification.configuration.OperatingSystem;
import fr.jcgay.notification.notifier.DoNothingNotifier;
import org.slf4j.Logger;

import java.util.Properties;
import java.util.Set;

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
 *  .initNotifier();
 * }</pre>
 */
public class SendNotification {

    private static final Logger LOGGER = getLogger(SendNotification.class);

    private final NotifierProvider provider;

    private ConfigurationReader configuration;
    private Application application;
    private ChosenNotifiers chosenNotifiers;
    private Properties additionalConfiguration;

    SendNotification(ConfigurationReader configuration, OperatingSystem currentOs) {
        this(configuration, new NotifierProvider(currentOs));
    }

    SendNotification(ConfigurationReader configuration, NotifierProvider provider) {
        this.configuration = configuration;
        this.provider = provider;
    }

    public SendNotification() {
        this(ConfigurationReader.atPath(System.getProperty("user.home") + "/.send-notification"), new OperatingSystem());
    }

    @VisibleForTesting
    DiscoverableNotifier chooseNotifier() {
        Properties properties = configuration.get();
        LOGGER.debug("Configuration is: {}.", properties);

        mergeConfigurations(properties);
        maySetNotifierFromPropertyConfiguration(properties);

        if (chosenNotifiers != null) {
            LOGGER.debug("Notifications will be send to: {} for application: {}.", chosenNotifiers, application);
            return provider.byName(chosenNotifiers, properties, application);
        }

        return defaultNotifier(properties);
    }

    /**
     * Build a {@link fr.jcgay.notification.Notifier}.
     *
     * @return notifier based on provided configuration.
     */
    public Notifier initNotifier() {
        return chooseNotifier().init();
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
        this.chosenNotifiers = ChosenNotifiers.from(chosenNotifier);
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

    private DiscoverableNotifier defaultNotifier(Properties properties) {
        Set<DiscoverableNotifier> all = provider.available(properties, application);
        for (DiscoverableNotifier candidate : all) {
            boolean isAvailable = candidate.tryInit();
            LOGGER.debug("{} is available ? {}.", candidate, isAvailable);
            if (isAvailable) {
                return candidate;
            }
        }
        return DoNothingNotifier.doNothing();
    }

    private void mergeConfigurations(Properties properties) {
        if (additionalConfiguration != null) {
            LOGGER.debug("Overriding previous configuration with: {}.", additionalConfiguration);
            properties.putAll(additionalConfiguration);
        }
    }

    private void maySetNotifierFromPropertyConfiguration(Properties properties) {
        String candidate = (String) properties.get("notifier.implementation");
        if (chosenNotifiers == null && candidate != null) {
            chosenNotifiers = ChosenNotifiers.from(candidate);
        }
    }
}
