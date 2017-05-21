package fr.jcgay.notification.notifier.pushbullet;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

@AutoValue
public abstract class PushbulletConfiguration {

    public abstract String key();

    @Nullable
    public abstract String device();

    @Nullable
    public abstract String email();

    PushbulletConfiguration() {
        // prevent external subclasses
    }

    public static PushbulletConfiguration create(Properties properties) {
        checkNotNull(properties, "Cannot create Pushbullet configuration without user configuration.");

        return new AutoValue_PushbulletConfiguration(
                properties.getProperty("notifier.pushbullet.apikey"),
                properties.getProperty("notifier.pushbullet.device"),
                properties.getProperty("notifier.pushbullet.email")
        );
    }
}
