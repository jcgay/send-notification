package fr.jcgay.notification;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Application {

    public abstract String id();

    public abstract String name();

    public abstract long timeout();

    public abstract Icon icon();

    Application() {
        // prevent external subclasses
    }

    public static Builder builder(String id, String name, Icon icon) {
        return new Builder(id, name, icon);
    }

    public static class Builder {

        private final String id;
        private final String name;
        private final Icon icon;
        private Long timeout;

        private Builder(String id, String name, Icon icon) {
            this.id = id;
            this.name = name;
            this.icon = icon;
        }

        public Builder withTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public Application build() {
            return new AutoValue_Application(id, name, timeout == null ? -1 : timeout, icon);
        }
    }
}
