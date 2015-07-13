package fr.jcgay.notification;

/**
 * Notifier can send notification(s). <br />
 * It must be closed to release resources when it is not needed anymore.
 *
 * <pre>{@code
 * Notifier notifier = ...;
 * try {
 *     notifier.send(notification);
 * } finally {
 *     notifier.close;
 * }
 * }</pre>
 */
public interface Notifier {

    /**
     * Send a notification.
     *
     * @param notification a notification.
     *
     * @throws fr.jcgay.notification.SendNotificationException when something fails.
     */
    void send(Notification notification);

    /**
     * Close the notifier. <br />
     * This method must be called to release resources when the notifier is not needed anymore.
     */
    void close();
}
