package fr.jcgay.notification.notifier;

import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;

public class DoNothingNotifier implements DiscoverableNotifier {

    private static final DoNothingNotifier INSTANCE = new DoNothingNotifier();

    public static DoNothingNotifier doNothing() {
        return INSTANCE;
    }

    private DoNothingNotifier() {
    }

    @Override
    public Notifier init() {
        return this;
    }

    @Override
    public void send(Notification notification) {
        // do nothing
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public boolean tryInit() {
        return true;
    }
}
