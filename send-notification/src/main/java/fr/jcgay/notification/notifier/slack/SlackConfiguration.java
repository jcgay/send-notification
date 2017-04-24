package fr.jcgay.notification.notifier.slack;

import com.google.auto.value.AutoValue;

import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

@AutoValue
public abstract class SlackConfiguration {

    /**
     * @return token to access Slack APIs
     * @see <a href="https://api.slack.com/custom-integrations/legacy-tokens">Legacy Token generator</a>
     */
    public abstract String token();

    /**
     * @return the channel name to send messages to (you should use your own @handle)
     */
    public abstract String channel();

    SlackConfiguration() {
        // prevent external subclasses
    }

    public static SlackConfiguration create(Properties properties) {
        checkNotNull(properties, "Cannot create Slack configuration without user configuration.");

        return new AutoValue_SlackConfiguration(
            properties.getProperty("notifier.slack.token"),
            properties.getProperty("notifier.slack.channel")
        );
    }
}
