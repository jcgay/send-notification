package fr.jcgay.notification.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;
import fr.jcgay.notification.SendNotificationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

    @Parameter(names = {"-l", "--level"}, description = "notification level (INFO, WARNING, ERROR)", converter = NotificationLevelConverter.class)
    private Notification.Level level;

    @Parameter(names = {"-h", "--help"}, help = true, description = "show help")
    private boolean help;

    @Parameter(names = {"-v", "--version"}, help = true, description = "show version")
    private boolean showVersion;

    public static void main(String[] args) {
        SendNotificationCli run = new SendNotificationCli();
        JCommander command = new JCommander(run, args);
        if (run.help) {
            command.usage();
            System.exit(0);
        }
        if (run.showVersion) {
            System.out.println("Send Notification CLI " + readVersion());
            System.exit(0);
        }
        run.fireNotifications();
    }

    private void fireNotifications() {
        if (notifiers != null && !notifiers.isEmpty()) {
            for (String notifier : notifiers) {
                sendNotificationUsing(buildNotifier(notifier));
            }
        } else {
            sendNotificationUsing(buildNotifier());
        }
    }

    private void sendNotificationUsing(Notifier notify) {
        try {
            Notification.Builder notification = Notification.builder()
                .title(title)
                .message(message)
                .icon(notificationIcon())
                .subtitle(subtitle);
            if (level != null) {
                notification.level(level);
            }
            notify.send(notification.build());
        } finally {
            notify.close();
        }
    }

    private Notifier buildNotifier() {
        return new SendNotification()
                .setApplication(application())
                .initNotifier();
    }

    private static Notifier buildNotifier(String notifier) {
        return new SendNotification()
                        .setApplication(application())
                        .setChosenNotifier(notifier)
                        .initNotifier();
    }

    private Icon notificationIcon() {
        try {
            return Icon.create(icon.toURI().toURL(), "send-notification-" + icon.getName());
        } catch (MalformedURLException e) {
            throw new SendNotificationException("Cannot build URL from file: " + icon, e);
        }
    }

    private static Application application() {
        return Application.builder()
            .id("application/x-vnd-jcgay.send-notification")
            .name("Send Notification")
            .icon(icon())
            .build();
    }

    private static Icon icon() {
        return Icon.create(SendNotificationCli.class.getResource("/brochure5.png"), "send-notification-cli");
    }

    private static String readVersion() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(SendNotificationCli.class.getResourceAsStream("/version.txt")));
        try {
            return reader.readLine();
        } catch (IOException e) {
            return "";
        }
    }
}
