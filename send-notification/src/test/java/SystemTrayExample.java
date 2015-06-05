import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

public class SystemTrayExample {

    public static void main(String[] args) {

        URL icon = SystemTrayExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder()
            .id("systemtray-example")
            .name("SystemTray Example")
            .icon(Icon.create(icon, "app"))
            .timeout(2000)
            .build();

        Notifier notifier = new SendNotification()
                .setApplication(application)
                .setChosenNotifier("systemtray")
                .chooseNotifier();

        Notification notification = Notification.builder()
            .title("SystemTray Notification")
            .message("Hello !")
            .icon(Icon.create(icon, "ok"))
            .build();

        notifier.init();
        notifier.send(notification);
        notifier.close();
    }
}
