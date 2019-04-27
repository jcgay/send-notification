import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;
import java.util.concurrent.TimeUnit;

class NotifySendExample {

    public static void main(String[] args) {

        URL icon = NotifySendExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder()
            .id("notify-send-example")
            .name("Notify Send Example")
            .icon(Icon.create(icon, "app"))
            .timeout(TimeUnit.SECONDS.toMillis(3))
            .build();

        Notifier notifier = new SendNotification()
                .setApplication(application)
                .setChosenNotifier("notifysend")
                .initNotifier();

        Notification notification = Notification.builder()
            .title("Notify Send Notification")
            .message("Hello !")
            .icon(Icon.create(icon, "ok"))
            .build();

        notifier.send(notification);
        notifier.close();
    }
}
