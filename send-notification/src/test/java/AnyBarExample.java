import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

public class AnyBarExample {

    public static void main(String[] args) {
        URL icon = AnyBarExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder("AnyBar-example", "AnyBar Example", Icon.create(icon, "error")).build();

        Notifier notifier = new SendNotification()
            .setApplication(application)
            .setChosenNotifier("anybar")
            .chooseNotifier();

        Notification notification = Notification.builder("AnyBar Notification", "Hello !", Icon.create(icon, "error")).build();

        notifier.init();
        notifier.send(notification);
        notifier.close();
    }
}
