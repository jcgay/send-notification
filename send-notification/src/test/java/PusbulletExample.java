import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

public class PusbulletExample {

    public static void main(String[] args) {

        URL icon = PusbulletExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder("pushbullet-example", "Pushbullet Example", Icon.create(icon, "app")).build();

        Notifier notifier = new SendNotification()
                .setApplication(application)
                .setChosenNotifier("pushbullet")
                .chooseNotifier();

        Notification notification = Notification.builder("Pushbullet Notification", "Hello !", Icon.create(icon, "ok")).build();

        notifier.init();
        notifier.send(notification);
        notifier.close();
    }
}
