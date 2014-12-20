package fr.jcgay.notification;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

/**
 * A notification to send.
 */
@AutoValue
public abstract class Notification {

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
     * Notification subtitle. <br />
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

    Notification() {
        // prevent external subclasses
    }

    public static Builder builder(String title, String message, Icon icon) {
        return new Builder(title, message, icon);
    }

    public static class Builder {

        private final String title;
        private final String message;
        private final Icon icon;
        private String subtitle;

        private Builder(String title, String message, Icon icon) {
            this.title = title;
            this.message = message;
            this.icon = icon;
        }

        public Builder withSubtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Notification build() {
            return new AutoValue_Notification(message, title, subtitle, icon);
        }
    }
}
