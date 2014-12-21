package fr.jcgay.notification.notifier.notifysend;

import com.google.common.base.Joiner;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.notifier.executor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NotifySendNotifier implements Notifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifySendNotifier.class);

    private static final String CMD_TIMEOUT = "-t";
    private static final String CMD_ICON = "-i";
    private static final String CMD_URGENCY = "-u";

    private final Application application;
    private final NotifySendConfiguration configuration;
    private final Executor executor;

    public NotifySendNotifier(Application application, NotifySendConfiguration configuration, Executor executor) {
        LOGGER.debug("Configuring notify-send for application {}: {}.", application, configuration);
        this.application = application;
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public void init() {
        // do nothing
    }

    @Override
    public void send(Notification notification) {
        List<String> commands = new ArrayList<String>();
        commands.add(configuration.bin());
        commands.add(notification.title());
        commands.add(notification.message());
        if (application.timeout() != -1) {
            commands.add(CMD_TIMEOUT);
            commands.add(String.valueOf(application.timeout()));
        }
        commands.add(CMD_ICON);
        commands.add(notification.icon().asPath());
        commands.add(CMD_URGENCY);
        commands.add(toUrgency(notification.level()));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Will execute command line: " + Joiner.on(" ").join(commands));
        }

        try {
            executor.exec(commands.toArray(new String[commands.size()]));
        } catch (RuntimeException e) {
            throw new NotifySendNotificationException("Error while sending notification to notify-send.", e.getCause());
        }
    }

    @Override
    public void close() {
        // do nothing
    }

    private static String toUrgency(Notification.Level level) {
        switch (level) {
            case WARNING:
            case ERROR:
                return "critical";
            default:
                return "normal";
        }
    }
}
