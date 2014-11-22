package fr.jcgay.notification;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Notification {

    public abstract String message();

    public abstract String title();

    @Nullable
    public abstract String subtitle();

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
