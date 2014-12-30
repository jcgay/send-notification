package fr.jcgay.notification.notifier.notifu;

import fr.jcgay.notification.UrlSendNotificationException;

public class NotifuException extends UrlSendNotificationException {

    public NotifuException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/notifu";
    }
}
