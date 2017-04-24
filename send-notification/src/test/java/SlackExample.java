import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

import java.net.URL;

class SlackExample {

    public static void main(String[] args) {

        URL icon = SlackExample.class.getResource("/image/dialog-clean.png");

        Application application = Application.builder()
            .id("slack-example")
            .name("Slack Example")
            .icon(Icon.create(icon, "app"))
            .build();

        Notifier notifier = new SendNotification()
                .setApplication(application)
                .setChosenNotifier("slack")
                .initNotifier();

        Notification notification = Notification.builder()
            .title("Slack Notification")
            .message("Hello !")
            .subtitle("Subtitle")
            .icon(Icon.create(icon, "ok"))
            .build();

        notifier.send(notification);
        notifier.close();
    }
}
