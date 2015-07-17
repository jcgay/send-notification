import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;
import fr.jcgay.notification.notifier.additional.AdditionalNotifier;

import java.net.URL;

public class AdditionalNotifierExample {

    public static void main(String[] args) {
        URL icon = AdditionalNotifier.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder()
            .id("additional-example")
            .name("additional Example")
            .icon(Icon.create(icon, "app"))
            .build();

        Notifier notifier = new SendNotification()
            .setApplication(application)
            .setChosenNotifier("growl,notificationcenter")
            .initNotifier();

        Notification notification = Notification.builder()
            .title("Multiple Notification")
            .message("Hello !")
            .icon(Icon.create(icon, "ok"))
            .build();

        notifier.send(notification);
        notifier.close();
    }

}
