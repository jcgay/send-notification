package fr.jcgay.notification.notifier.notifu;

import com.google.auto.value.AutoValue;

import java.util.Properties;

@AutoValue
public abstract class NotifuConfiguration {

    private static final NotifuConfiguration DEFAULT = new AutoValue_NotifuConfiguration("notifu64");

    public abstract String bin();

    NotifuConfiguration() {
        // prevent external subclasses
    }

    public static NotifuConfiguration byDefault() {
        return DEFAULT;
    }

    public static NotifuConfiguration create(Properties properties) {
        if (properties == null) {
            return byDefault();
        }
        return new AutoValue_NotifuConfiguration(
                properties.getProperty("notifier.notifu.path", DEFAULT.bin())
        );
    }
}
