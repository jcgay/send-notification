package fr.jcgay.notification.notifier.snarl;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.Properties;

@AutoValue
public abstract class SnarlConfiguration {

    private static final SnarlConfiguration DEFAULT = new AutoValue_SnarlConfiguration("localhost", 9887, null);

    public abstract String host();

    public abstract int port();

    @Nullable
    public abstract String applicationPassword();

    SnarlConfiguration() {
        // prevent external subclasses
    }

    public static SnarlConfiguration byDefault() {
        return DEFAULT;
    }

    public static SnarlConfiguration create(Properties properties) {
        if (properties == null) {
            return byDefault();
        }

        return new AutoValue_SnarlConfiguration(
                properties.getProperty("notifier.snarl.host", DEFAULT.host()),
                Integer.valueOf(properties.getProperty("notifier.snarl.port", String.valueOf(DEFAULT.port()))),
                properties.getProperty("notifier.snarl.appPassword", DEFAULT.applicationPassword())
        );
    }
}
