import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

class PusbulletExample {

    public static void main(String[] args) {

        URL icon = PusbulletExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder()
            .id("pushbullet-example")
            .name("Pushbullet Example")
            .icon(Icon.create(icon, "app"))
            .build();

        Notifier notifier = new SendNotification()
                .setApplication(application)
                .setChosenNotifier("pushbullet")
                .initNotifier();

        Notification notification = Notification.builder()
            .title("Pushbullet Notification")
            .message("Hello !")
            .icon(Icon.create(icon, "ok"))
            .build();

        notifier.send(notification);
        notifier.close();
    }
}
