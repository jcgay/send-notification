package fr.jcgay.notification.notifier;

import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;

public class DoNothingNotifier implements Notifier {

    private static DoNothingNotifier INSTANCE = new DoNothingNotifier();

    public static DoNothingNotifier doNothing() {
        return INSTANCE;
    }

    private DoNothingNotifier() {
    }

    @Override
    public void init() {
        // do nothing
    }

    @Override
    public void send(Notification notification) {
        // do nothing
    }

    @Override
    public void close() {
        // do nothing
    }
}
