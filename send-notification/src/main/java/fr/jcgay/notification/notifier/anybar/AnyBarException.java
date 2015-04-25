package fr.jcgay.notification.notifier.anybar;

import fr.jcgay.notification.UrlSendNotificationException;

public class AnyBarException extends UrlSendNotificationException {

    public AnyBarException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/anybar";
    }
}
