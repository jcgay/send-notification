import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

public class SystemTrayExample {

    public static void main(String[] args) {

        URL icon = SystemTrayExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder("systemtray-example", "SystemTray Example", Icon.create(icon, "app"))
                .withTimeout(2000).build();

        Notifier notifier = new SendNotification()
                .setApplication(application)
                .setChosenNotifier("systemtray")
                .chooseNotifier();

        Notification notification = Notification.builder("SystemTray Notification", "Hello !", Icon.create(icon, "ok")).build();

        notifier.init();
        notifier.send(notification);
        notifier.close();
    }
}
