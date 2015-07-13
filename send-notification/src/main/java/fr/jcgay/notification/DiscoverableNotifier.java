package fr.jcgay.notification;

public interface DiscoverableNotifier extends Notifier {

    /**
     * Initialize before usage. <br />
     * This method must be called before trying to send a notification. <br />
     * Calling {@code init()} multiple time should not fail. <br />
     *
     * For example a notifier can open a connection to register the application, or it can do nothing.
     *
     * @throws fr.jcgay.notification.SendNotificationException when something fails.
     */
    Notifier init();

    boolean tryInit();
}
