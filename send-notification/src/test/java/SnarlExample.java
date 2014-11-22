import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

public class SnarlExample {

    public static void main(String[] args) {

        URL icon = SnarlExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder("snarl-example", "Snarl Example", Icon.create(icon, "app")).build();

        Notifier notifier = new SendNotification()
                .setApplication(application)
                .setChosenNotifier("snarl")
                .chooseNotifier();

        Notification notification = Notification.builder("Snarl Notification", "Hello !", Icon.create(icon, "ok")).build();

        notifier.init();
        notifier.send(notification);
        notifier.close();
    }
}
