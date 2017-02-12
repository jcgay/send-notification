package fr.jcgay.notification.notifier.additional;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.MultipleSendNotificationException;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;

public class AdditionalNotifier implements DiscoverableNotifier {

    private static final Logger LOGGER = getLogger(AdditionalNotifier.class);

    private final Set<DiscoverableNotifier> secondary;
    private final DiscoverableNotifier primary;
    private final boolean notifyWithSecondaryNotifiers;

    public AdditionalNotifier(DiscoverableNotifier primary, Set<DiscoverableNotifier> secondary) {
        checkArgument(!secondary.isEmpty());
        this.primary = checkNotNull(primary);
        this.secondary = secondary;
        this.notifyWithSecondaryNotifiers = Boolean.valueOf(System.getProperty("notifyAll"));
    }

    @Override
    public Notifier init() {
        List<Exception> errors = new ArrayList<Exception>();

        safeInit(primary, errors);

        if (notifyWithSecondaryNotifiers) {
            for (DiscoverableNotifier notifier : secondary) {
                safeInit(notifier, errors);
            }
        }

        failIfNotEmpty(errors);

        return this;
    }

    @Override
    public boolean tryInit() {
        return false;
    }

    @Override
    public void send(Notification notification) {
        List<Exception> errors = new ArrayList<Exception>();

        safeSend(primary, notification, errors);

        if (notifyWithSecondaryNotifiers) {
            for (DiscoverableNotifier notifier : secondary) {
                safeSend(notifier, notification, errors);
            }
        }

        failIfNotEmpty(errors);
    }

    @Override
    public void close() {
        List<Exception> errors = new ArrayList<Exception>();

        safeClose(primary, errors);

        if (notifyWithSecondaryNotifiers) {
            for (DiscoverableNotifier notifier : secondary) {
                safeClose(notifier, errors);
            }
        }

        failIfNotEmpty(errors);
    }

    @Override
    public boolean isPersistent() {
        if (notifyWithSecondaryNotifiers) {
            boolean result = primary.isPersistent();
            for (DiscoverableNotifier notifier : secondary) {
                result |= notifier.isPersistent();
            }
            return result;
        }
        return primary.isPersistent();
    }

    public static void safeClose(DiscoverableNotifier notifier, List<Exception> errors) {
        try {
            notifier.close();
        } catch (RuntimeException e) {
            LOGGER.debug("Error closing {}.", notifier, e);
            errors.add(e);
        }
    }

    private static void failIfNotEmpty(List<Exception> errors) {
        if (!errors.isEmpty()) {
            throw new MultipleSendNotificationException(errors);
        }
    }

    private static void safeSend(DiscoverableNotifier notifier, Notification notification, List<Exception> errors) {
        try {
            notifier.send(notification);
        } catch (RuntimeException e) {
            LOGGER.debug("Error sending notification {} with {}.", notification, notifier, e);
            errors.add(e);
        }
    }

    public static void safeInit(DiscoverableNotifier notifier, List<Exception> errors) {
        try {
            notifier.init();
        } catch (RuntimeException e) {
            LOGGER.debug("Error initializing {}.", notifier, e);
            errors.add(e);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(secondary, primary, notifyWithSecondaryNotifiers);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AdditionalNotifier other = (AdditionalNotifier) obj;
        return Objects.equal(this.secondary, other.secondary)
            && Objects.equal(this.primary, other.primary)
            && Objects.equal(this.notifyWithSecondaryNotifiers, other.notifyWithSecondaryNotifiers);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("secondary", secondary)
            .add("primary", primary)
            .add("notifyWithSecondaryNotifiers", notifyWithSecondaryNotifiers)
            .toString();
    }
}
