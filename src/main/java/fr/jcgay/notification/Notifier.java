package fr.jcgay.notification;

public interface Notifier {

    void init();

    void send(Notification notification);

    void close();
}
