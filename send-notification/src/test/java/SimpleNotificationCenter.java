import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

public class SimpleNotificationCenter {

    public static void main(String[] args) {

        URL icon = TerminalNotifierExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder()
            .id("notification-center-example")
            .name("Notification Center Example")
            .icon(Icon.create(icon, "app"))
            .build();

        Notifier notifier = new SendNotification()
            .setApplication(application)
            .setChosenNotifier("simplenc")
            .chooseNotifier();

        Notification notification = Notification.builder()
            .title("Notification Center Notification")
            .message("Hello !")
            .icon(Icon.create(icon, "ok"))
            .subtitle("subtitle")
            .build();

        notifier.send(notification);
    }
}
