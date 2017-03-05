package fr.jcgay.notification.notifier.notify;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import dorkbox.notify.Notify;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import org.slf4j.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;

public class NotifyNotifier implements DiscoverableNotifier {

    private static final Logger LOGGER = getLogger(NotifyNotifier.class);

    private final Application application;
    private final NotifyConfiguration configuration;

    private boolean skipNotifications;

    public NotifyNotifier(Application application, NotifyConfiguration configuration) {
        LOGGER.debug("Configuring notify: {}.", configuration);
        this.configuration = configuration;
        this.application = application;
    }

    @Override
    public Notifier init() {
        if (isHeadless()) {
            skipNotifications = true;
        }
        return this;
    }

    @Override
    public boolean tryInit() {
        init();
        return !skipNotifications;
    }

    @Override
    public void send(Notification notification) {
        Notify notify = Notify.create()
            .title(notification.title())
            .text(notification.message())
            .graphic(notification.icon().toImage())
            .position(configuration.position())
            .hideAfter((int) (application.timeout() == -1 ? SECONDS.toMillis(3) : application.timeout()));

        if (configuration.withDarkStyle()) {
            notify.darkStyle();
        }

        notify.show();
    }

    @Override
    public void close() {
        if (!skipNotifications) {
            try {
                Thread.sleep(application.timeout() == -1 ? SECONDS.toMillis(3) : application.timeout());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static boolean isHeadless() {
        return "true".equals(System.getProperty("java.awt.headless"));
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(application, configuration, skipNotifications);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NotifyNotifier other = (NotifyNotifier) obj;
        return Objects.equal(this.application, other.application)
            && Objects.equal(this.configuration, other.configuration)
            && Objects.equal(this.skipNotifications, other.skipNotifications);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("application", application)
            .add("configuration", configuration)
            .add("skipNotifications", skipNotifications)
            .toString();
    }
}
