package fr.jcgay.notification.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;
import fr.jcgay.notification.SendNotificationException;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public class SendNotificationCli {

    @Parameter(description = "notifier(s)")
    private List<String> notifiers;

    @Parameter(names = {"-t", "--title"}, description = "notification title", required = true)
    private String title;

    @Parameter(names = {"-m", "--message"}, description = "notification message", required = true)
    private String message;

    @Parameter(names = {"-i", "--icon"}, description = "notification icon", required = true, converter = FileConverter.class)
    private File icon;

    @Parameter(names = {"-s", "--subtitle"}, description = "notification subtitle")
    private String subtitle;

    public static void main(String[] args) {
        SendNotificationCli run = new SendNotificationCli();
        new JCommander(run, args);
        run.fireNotifications();
    }

    private void fireNotifications() {
        if (notifiers != null && !notifiers.isEmpty()) {
            for (String notifier : notifiers) {
                sendNotificationUsing(buildNotifier(notifier));
            }
        } else {
            sendNotificationUsing(buildNotifier(autoSelectNotifier()));
        }
    }

    private static String autoSelectNotifier() {
        return null;
    }

    private void sendNotificationUsing(Notifier notify) {
        notify.init();
        try {
            notify.send(
                Notification.builder(title, message, notificationIcon())
                    .withSubtitle(subtitle)
                    .build()
            );
        } finally {
            notify.close();
        }
    }

    private static Notifier buildNotifier(String notifier) {
        return new SendNotification()
                        .setApplication(application())
                        .setChosenNotifier(notifier)
                        .chooseNotifier();
    }

    private Icon notificationIcon() {
        try {
            return Icon.create(icon.toURI().toURL(), "send-notification-" + icon.getName());
        } catch (MalformedURLException e) {
            throw new SendNotificationException("Cannot build URL from file: " + icon, e);
        }
    }

    private static Application application() {
        return Application.builder("application/x-vnd-jcgay.send-notification", "Send Notification", icon()).build();
    }

    private static Icon icon() {
        return Icon.create(SendNotificationCli.class.getResource("/brochure5.png"), "send-notification-cli");
    }
}
