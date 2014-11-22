package fr.jcgay.notification.notifier.pushbullet;

import fr.jcgay.notification.UrlSendNotificationException;

public class PushbulletNotificationException extends UrlSendNotificationException {

    public PushbulletNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PushbulletNotificationException(String message) {
        super(message);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/Pushbullet";
    }
}
