import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class NotifySendExample {

    public static void main(String[] args) {

        URL icon = NotifySendExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder(
                "notify-send-example", "Notify Send Example", Icon.create(icon, "app")
        ).withTimeout(TimeUnit.SECONDS.toMillis(3)).build();

        Notifier notifier = new SendNotification()
                .setApplication(application)
                .setChosenNotifier("notifysend")
                .chooseNotifier();

        Notification notification = Notification.builder(
                "Notify Send Notification", "Hello !", Icon.create(icon, "ok")
        ).build();

        notifier.send(notification);
    }
}
