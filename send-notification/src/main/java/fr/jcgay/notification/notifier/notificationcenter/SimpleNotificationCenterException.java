package fr.jcgay.notification.notifier.notificationcenter;

import fr.jcgay.notification.UrlSendNotificationException;

public class SimpleNotificationCenterException extends UrlSendNotificationException {

    public SimpleNotificationCenterException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/notification-center";
    }
}
