package fr.jcgay.notification.notifier.toaster;

import fr.jcgay.notification.UrlSendNotificationException;

public class ToasterNotificationException extends UrlSendNotificationException {

    public ToasterNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/toaster";
    }
}
