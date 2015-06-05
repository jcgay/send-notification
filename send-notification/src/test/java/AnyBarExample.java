import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

public class AnyBarExample {

    public static void main(String[] args) {
        URL icon = AnyBarExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder()
            .id("AnyBar-example")
            .name("AnyBar Example")
            .icon(Icon.create(icon, "error"))
            .build();

        Notifier notifier = new SendNotification()
            .setApplication(application)
            .setChosenNotifier("anybar")
            .chooseNotifier();

        Notification notification = Notification.builder()
            .title("AnyBar Notification")
            .message("Hello !")
            .icon(Icon.create(icon, "error"))
            .build();

        notifier.init();
        notifier.send(notification);
        notifier.close();
    }
}
