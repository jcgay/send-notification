package fr.jcgay.notification.notifier.growl;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.Properties;

@AutoValue
public abstract class GrowlConfiguration {

    private static final GrowlConfiguration DEFAULT = new AutoValue_GrowlConfiguration(
            "localhost", null, 23053
    );

    public abstract String host();

    @Nullable
    public abstract String password();

    public abstract int port();

    GrowlConfiguration() {
        // prevent external subclasses
    }

    public static GrowlConfiguration byDefault() {
        return DEFAULT;
    }

    public static GrowlConfiguration create(Properties properties) {
        if (properties == null) {
            return byDefault();
        }

        return new AutoValue_GrowlConfiguration(
                properties.getProperty("notifier.growl.host", DEFAULT.host()),
                properties.getProperty("notifier.growl.password"),
                Integer.valueOf(properties.getProperty("notifier.growl.port", String.valueOf(DEFAULT.port())))
        );
    }
}
