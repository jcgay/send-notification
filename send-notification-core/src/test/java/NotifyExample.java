import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

public class NotifyExample {

    public static void main(String[] args) {
        URL icon = SystemTrayExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder()
            .id("notify-example")
            .name("Notify Example")
            .icon(Icon.create(icon, "app"))
            .build();

        Notifier notifier = new SendNotification()
            .setApplication(application)
            .setChosenNotifier("notify")
            .initNotifier();

        Notification notification = Notification.builder()
            .title("Notify Notification")
            .message("Hello !")
            .icon(Icon.create(icon, "ok"))
            .build();

        notifier.send(notification);
        notifier.close();
    }
}
