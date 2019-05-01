package fr.jcgay.notification.notifier.kdialog;

import fr.jcgay.notification.UrlSendNotificationException;

public class KdialogException extends UrlSendNotificationException {

    public KdialogException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/kdialog";
    }
}
