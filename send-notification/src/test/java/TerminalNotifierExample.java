import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

public class TerminalNotifierExample {

    public static void main(String[] args) {

        URL icon = TerminalNotifierExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder(
                "terminal-notifier-example", "Terminal Notifier Example", Icon.create(icon, "app")
        ).build();

        Notifier notifier = new SendNotification()
                .setApplication(application)
                .setChosenNotifier("notificationcenter")
                .chooseNotifier();

        Notification notification = Notification.builder(
                "Terminal Notifier Notification", "Hello !", Icon.create(icon, "ok")
        ).withSubtitle("subtitle").build();

        notifier.send(notification);
    }
}
