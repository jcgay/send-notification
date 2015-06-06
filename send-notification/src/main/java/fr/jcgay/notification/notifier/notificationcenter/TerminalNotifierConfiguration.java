package fr.jcgay.notification.notifier.notificationcenter;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.Properties;

@AutoValue
public abstract class TerminalNotifierConfiguration {

    private static final TerminalNotifierConfiguration DEFAULT = new AutoValue_TerminalNotifierConfiguration(
            "terminal-notifier", "com.apple.Terminal", null
    );

    public abstract String bin();

    public abstract String activateApplication();

    @Nullable
    public abstract String sound();

    TerminalNotifierConfiguration() {
        // prevent external subclasses
    }

    public static TerminalNotifierConfiguration byDefault() {
        return DEFAULT;
    }

    public static TerminalNotifierConfiguration create(Properties properties) {
        if (properties == null) {
            return byDefault();
        }

        return new AutoValue_TerminalNotifierConfiguration(
                properties.getProperty("notifier.notification-center.path", DEFAULT.bin()),
                properties.getProperty("notifier.notification-center.activate", DEFAULT.activateApplication()),
                properties.getProperty("notifier.notification-center.sound", DEFAULT.sound())
        );
    }
}
