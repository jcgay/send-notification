package fr.jcgay.notification;

/**
 * Notifier can send notification(s). <br />
 * It must be initialized before usage and closed to release resources when it is not needed anymore.
 *
 * <pre>{@code
 * Notifier notifier = ...;
 * notifier.init();
 * try {
 *     notifier.send(notification);
 * } finally {
 *     notifier.close;
 * }
 * }</pre>
 */
public interface Notifier {

    /**
     * Initialize before usage. <br />
     * This method must be called (and only once) before trying to send a notification. <br />
     *
     * For example a notifier can open a connection to register the application, or it can do nothing.
     *
     * @throws fr.jcgay.notification.SendNotificationException when something fails.
     */
    void init();

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
