package fr.jcgay.notification.notifier.notifysend;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.notifier.executor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NotifySendNotifier implements DiscoverableNotifier {

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
    public Notifier init() {
        return this;
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

        try {
            executor.exec(commands.toArray(new String[commands.size()]));
        } catch (RuntimeException e) {
            throw new NotifySendNotificationException("Error while sending notification to notify-send.", e.getCause());
        }
    }

    @Override
    public void close() {
        executor.close();
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public boolean tryInit() {
        List<String> commands = new ArrayList<String>();
        commands.add(configuration.bin());
        commands.add("-v");

        return executor.tryExec(commands.toArray(new String[commands.size()]));
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

    @Override
    public int hashCode() {
        return Objects.hashCode(application, configuration);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NotifySendNotifier other = (NotifySendNotifier) obj;
        return Objects.equal(this.application, other.application)
            && Objects.equal(this.configuration, other.configuration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("application", application)
            .add("configuration", configuration)
            .toString();
    }
}
