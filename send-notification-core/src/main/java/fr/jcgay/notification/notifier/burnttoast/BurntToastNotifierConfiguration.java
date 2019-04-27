package fr.jcgay.notification.notifier.burnttoast;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.Properties;

@AutoValue
public abstract class BurntToastNotifierConfiguration {

    private static final BurntToastNotifierConfiguration DEFAULT = new AutoValue_BurntToastNotifierConfiguration(null);

    BurntToastNotifierConfiguration() {
        // prevent external subclasses
    }

    public static BurntToastNotifierConfiguration byDefault() {
        return DEFAULT;
    }

    public static BurntToastNotifierConfiguration create(Properties properties) {
        if (properties == null) {
            return byDefault();
        }

        return new AutoValue_BurntToastNotifierConfiguration(properties.getProperty("notifier.burnttoast.sound"));
    }

    @Nullable
    public abstract String sound();
}
