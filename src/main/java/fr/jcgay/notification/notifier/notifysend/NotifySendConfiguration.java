package fr.jcgay.notification.notifier.notifysend;

import com.google.auto.value.AutoValue;

import java.util.Properties;

@AutoValue
public abstract class NotifySendConfiguration {

    private static final NotifySendConfiguration DEFAULT = new AutoValue_NotifySendConfiguration("notify-send");

    public abstract String bin();

    NotifySendConfiguration() {
        // prevent external subclasses
    }

    public static NotifySendConfiguration byDefault() {
        return DEFAULT;
    }

    public static NotifySendConfiguration create(Properties properties) {
        if (properties == null) {
            return byDefault();
        }

        return new AutoValue_NotifySendConfiguration(properties.getProperty("notifier.notify-send.path", DEFAULT.bin()));
    }
}
