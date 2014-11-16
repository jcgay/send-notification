package fr.jcgay.notification;

public abstract class UrlSendNotificationException extends SendNotificationException {

    public UrlSendNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UrlSendNotificationException(String message) {
        super(message);
    }

    public abstract String getURL();

    @Override
    public String getMessage() {
        return String.format("%s%nCheck your configuration at: %s", super.getMessage(), getURL());
    }
}
