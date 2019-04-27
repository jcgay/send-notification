package fr.jcgay.notification;

/**
 * Base class used by exception tied to an URL to get more information and possible solution(s).
 */
public abstract class UrlSendNotificationException extends SendNotificationException {

    public UrlSendNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UrlSendNotificationException(String message) {
        super(message);
    }

    /**
     * URL to get more information about an error.
     *
     * @return documentation URL.
     */
    public abstract String getURL();

    @Override
    public String getMessage() {
        return String.format("%s%nCheck your configuration at: %s", super.getMessage(), getURL());
    }
}
