package fr.jcgay.notification.notifier.systemtray;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class SystemTrayNotifier implements Notifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemTrayNotifier.class);

    private final Application application;

    private boolean skipNotifications;
    private TrayIcon icon;

    public SystemTrayNotifier(Application application) {
        this.application = application;
    }

    @Override
    public void init() {
        if (!SystemTray.isSupported()) {
            skipNotifications = true;
            LOGGER.warn("SystemTray is not supported, skipping notifications...");
            return;
        }

        icon = new TrayIcon(createImage(application.icon().toByteArray()), application.name());
        icon.setImageAutoSize(true);

        try {
            SystemTray.getSystemTray().add(icon);
        } catch (AWTException e) {
            throw new SendNotificationException("Error initializing SystemTray Icon.", e);
        }
    }

    @Override
    public void send(Notification notification) {
        if (!skipNotifications) {
            icon.setImage(createImage(notification.icon().toByteArray()));
            icon.displayMessage(notification.title(), notification.message(), TrayIcon.MessageType.INFO);
        }
    }

    @Override
    public void close() {
        if (!skipNotifications) {
            try {
                Thread.sleep(application.timeout());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            SystemTray.getSystemTray().remove(icon);
        }
    }

    private Image createImage(byte[] imageData) {
        return Toolkit.getDefaultToolkit().createImage(imageData);
    }
}
