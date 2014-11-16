package fr.jcgay.notification.notifier.snarl;

import fr.jcgay.notification.UrlSendNotificationException;

public class SnarlNotificationException extends UrlSendNotificationException {

    public SnarlNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/Snarl";
    }
}
