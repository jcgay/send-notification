package fr.jcgay.notification.notifier.notify;

import com.google.auto.value.AutoValue;
import dorkbox.notify.Pos;

import java.util.Properties;

@AutoValue
public abstract class NotifyConfiguration {

    private static final NotifyConfiguration DEFAULT = new AutoValue_NotifyConfiguration(Pos.TOP_RIGHT, false);

    public abstract Pos position();

    public abstract boolean withDarkStyle();

    NotifyConfiguration() {
        // prevent external subclasses
    }

    public static NotifyConfiguration byDefault() {
        return DEFAULT;
    }

    public static NotifyConfiguration create(Properties properties) {
        if (properties == null) {
            return byDefault();
        }

        return new AutoValue_NotifyConfiguration(
            Pos.valueOf(properties.getProperty("notifier.notify.position", DEFAULT.position().name())),
            Boolean.valueOf(properties.getProperty("notifier.notify.darkstyle", String.valueOf(DEFAULT.withDarkStyle())))
        );
    }
}
