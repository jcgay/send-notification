package fr.jcgay.notification.notifier.systemtray;

import fr.jcgay.notification.UrlSendNotificationException;

public class SystemTrayNotificationException extends UrlSendNotificationException {

    public SystemTrayNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/System-tray";
    }
}
