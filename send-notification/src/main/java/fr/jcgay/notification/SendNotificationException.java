package fr.jcgay.notification;

/**
 * Unchecked exception thrown when something goes wrong.
 */
public class SendNotificationException extends RuntimeException {

    public SendNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendNotificationException(String message) {
        super(message);
    }
}
