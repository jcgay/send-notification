package fr.jcgay.notification.notifier.notifysend;

import fr.jcgay.notification.UrlSendNotificationException;

public class NotifySendNotificationException extends UrlSendNotificationException {

    public NotifySendNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/notify-send";
    }
}
