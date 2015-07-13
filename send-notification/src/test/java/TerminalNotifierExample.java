import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

public class TerminalNotifierExample {

    public static void main(String[] args) {

        URL icon = TerminalNotifierExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder()
            .id("terminal-notifier-example")
            .name("Terminal Notifier Example")
            .icon(Icon.create(icon, "app"))
            .build();

        Notifier notifier = new SendNotification()
                .setApplication(application)
                .setChosenNotifier("notificationcenter")
                .initNotifier();

        Notification notification = Notification.builder()
            .title("Terminal Notifier Notification")
            .message("Hello !")
            .icon(Icon.create(icon, "ok"))
            .subtitle("subtitle")
            .build();

        notifier.send(notification);
    }
}
