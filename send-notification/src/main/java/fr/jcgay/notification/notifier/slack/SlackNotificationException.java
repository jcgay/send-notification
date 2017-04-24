package fr.jcgay.notification.notifier.slack;

import fr.jcgay.notification.UrlSendNotificationException;

public class SlackNotificationException extends UrlSendNotificationException {

    public SlackNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SlackNotificationException(String message) {
        super(message);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/slack";
    }
}
