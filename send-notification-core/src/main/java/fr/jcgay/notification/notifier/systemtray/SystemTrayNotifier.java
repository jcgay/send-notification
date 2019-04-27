package fr.jcgay.notification.notifier.systemtray;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

import static java.util.concurrent.TimeUnit.SECONDS;

public class SystemTrayNotifier implements DiscoverableNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemTrayNotifier.class);

    private final Application application;

    private boolean skipNotifications;
    private TrayIcon icon;

    public SystemTrayNotifier(Application application) {
        LOGGER.debug("Configuring System Tray for application {}.", application);
        this.application = application;
    }

    @Override
    public Notifier init() {
        if (icon != null) {
            return this;
        }

        if (!SystemTray.isSupported()) {
            skipNotifications = true;
            LOGGER.warn("SystemTray is not supported, skipping notifications...");
            return this;
        }

        icon = new TrayIcon(createImage(application.icon().toByteArray()), application.name());
        icon.setImageAutoSize(true);

        try {
            SystemTray.getSystemTray().add(icon);
        } catch (AWTException e) {
            throw new SystemTrayNotificationException("Error initializing SystemTray Icon.", e);
        }

        return this;
    }

    @Override
    public void send(Notification notification) {
        if (!skipNotifications) {
            icon.setImage(createImage(notification.icon().toByteArray()));
            icon.displayMessage(notification.title(), notification.message(), toMessageType(notification.level()));
        }
    }

    @Override
    public void close() {
        if (!skipNotifications) {
            try {
                Thread.sleep(application.timeout() == -1 ? SECONDS.toMillis(2) : application.timeout());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            SystemTray.getSystemTray().remove(icon);
        }
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public boolean tryInit() {
        init();
        return !skipNotifications;
    }

    private static MessageType toMessageType(Notification.Level level) {
        switch (level) {
            case INFO:
                return MessageType.INFO;
            case WARNING:
                return MessageType.WARNING;
            case ERROR:
                return MessageType.ERROR;
            default:
                return MessageType.NONE;
        }
    }

    private Image createImage(byte[] imageData) {
        return Toolkit.getDefaultToolkit().createImage(imageData);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(application);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SystemTrayNotifier other = (SystemTrayNotifier) obj;
        return Objects.equal(this.application, other.application);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("application", application)
            .toString();
    }
}
