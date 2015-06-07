package fr.jcgay.notification.notifier.toaster;

import com.google.auto.value.AutoValue;

import java.util.Properties;

@AutoValue
public abstract class ToasterConfiguration {

    private static final ToasterConfiguration DEFAULT = new AutoValue_ToasterConfiguration("toast");

    abstract String bin();

    ToasterConfiguration() {
        // prevent external subclasses
    }

    public static ToasterConfiguration byDefault() {
        return DEFAULT;
    }

    public static ToasterConfiguration create(Properties properties) {
        if (properties == null) {
            return byDefault();
        }

        return new AutoValue_ToasterConfiguration(
            properties.getProperty("notifier.toaster.bin", DEFAULT.bin())
        );
    }
}
