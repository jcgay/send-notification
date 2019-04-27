package fr.jcgay.notification.notifier.kdialog;

import com.google.auto.value.AutoValue;

import java.util.Properties;

@AutoValue
public abstract class KdialogConfiguration {

    private static final KdialogConfiguration DEFAULT = new AutoValue_KdialogConfiguration("kdialog");

    public abstract String bin();

    KdialogConfiguration() {
        // prevent external subclasses
    }

    public static KdialogConfiguration byDefault() {
        return DEFAULT;
    }

    public static KdialogConfiguration create(Properties properties) {
        if (properties == null) {
            return byDefault();
        }

        return new AutoValue_KdialogConfiguration(
                properties.getProperty("notifier.kdialog.path", DEFAULT.bin())
        );
    }
}
