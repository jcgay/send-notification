package fr.jcgay.notification.notifier.anybar;

import com.google.auto.value.AutoValue;

import java.util.Properties;

@AutoValue
public abstract class AnyBarConfiguration {

    private static final AnyBarConfiguration DEFAULT = new AutoValue_AnyBarConfiguration("localhost", 1738);

    public abstract String host();

    public abstract int port();

    AnyBarConfiguration() {
        // prevent external subclasses
    }

    public static AnyBarConfiguration byDefault() {
        return DEFAULT;
    }

    public static AnyBarConfiguration create(Properties properties) {
        if (properties == null) {
            return byDefault();
        }

        return new AutoValue_AnyBarConfiguration(
            properties.getProperty("notifier.anybar.host", DEFAULT.host()),
            Integer.valueOf(
                properties.getProperty("notifier.anybar.port", String.valueOf(DEFAULT.port()))
            )
        );
    }
}
