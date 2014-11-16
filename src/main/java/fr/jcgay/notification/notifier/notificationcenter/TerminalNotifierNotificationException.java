package fr.jcgay.notification.notifier.notificationcenter;

import fr.jcgay.notification.UrlSendNotificationException;

public class TerminalNotifierNotificationException extends UrlSendNotificationException {

    public TerminalNotifierNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getURL() {
        return "https://github.com/jcgay/send-notification/wiki/terminal-notifier";
    }
}
