package fr.jcgay.notification;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

import static fr.jcgay.notification.Notification.Level.INFO;

/**
 * A notification to send.
 */
@AutoValue
public abstract class Notification {

    /**
     * Indicate notification priority.
     */
    public enum Level {INFO, WARNING, ERROR}

    /**
     * Notification message.
     *
     * @return message.
     */
    public abstract String message();

    /**
     * Notification title.
     *
     * @return title.
     */
    public abstract String title();

    /**
     * Notification subtitle. <br>
     * Not available for all notifier implementation.
     *
     * @return subtitle.
     */
    @Nullable
    public abstract String subtitle();

    /**
     * Notification icon.
     *
     * @return icon.
     */
    public abstract Icon icon();

    /**
     * Indicate the notification priority type.
     *
     * @return type
     */
    public abstract Level level();

    Notification() {
        // prevent external subclasses
    }

    public static Builder builder(String title, String message, Icon icon) {
        return builder()
            .title(title)
            .message(message)
            .icon(icon);
    }

    public static Builder builder() {
        return new AutoValue_Notification.Builder().level(INFO);
    }

    @AutoValue.Builder
    public interface Builder {
        Builder title(String title);
        Builder message(String message);
        Builder icon(Icon icon);
        Builder subtitle(String subtitle);
        Builder level(Level level);
        Notification build();
    }
}
