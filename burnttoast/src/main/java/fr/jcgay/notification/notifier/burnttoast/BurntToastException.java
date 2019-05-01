package fr.jcgay.notification.notifier.burnttoast;

import fr.jcgay.notification.UrlSendNotificationException;

public class BurntToastException extends UrlSendNotificationException {

    public BurntToastException(String message) {
        super(message);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/burnttoast";
    }
}
