package fr.jcgay.notification.notifier.notificationcenter;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.notifier.executor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SimpleNotificationCenterNotifier implements DiscoverableNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNotificationCenterNotifier.class);
    private static final String DOUBLE_QUOTE = "\"";

    private final TerminalNotifierConfiguration configuration;
    private final Executor executor;

    public SimpleNotificationCenterNotifier(TerminalNotifierConfiguration configuration, Executor executor) {
        LOGGER.debug("Configuring notification-center : {}.", configuration);
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
        commands.add("osascript");
        commands.add("-e");
        commands.add(buildAppleScript(notification));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Will execute command line: " + Joiner.on(" ").skipNulls().join(commands));
        }

        try {
            executor.exec(commands.toArray(new String[commands.size()]));
        } catch (RuntimeException e) {
            throw new SimpleNotificationCenterException("Error while sending notification to terminal-notifier", e.getCause());
        }
    }

    public String buildAppleScript(Notification notification) {
        StringBuilder script = new StringBuilder("display notification ");
        script.append(DOUBLE_QUOTE)
            .append(notification.message())
            .append(DOUBLE_QUOTE);
        if (configuration.sound() != null) {
            script.append(" sound name ")
                .append(DOUBLE_QUOTE)
                .append(configuration.sound())
                .append(DOUBLE_QUOTE);
        }
        script.append(" with title ")
            .append(DOUBLE_QUOTE)
            .append(notification.title())
            .append(DOUBLE_QUOTE);
        if (notification.subtitle() != null) {
            script.append(" subtitle ")
                .append(DOUBLE_QUOTE)
                .append(notification.subtitle())
                .append(DOUBLE_QUOTE);
        }
        return script.toString();
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public boolean tryInit() {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(configuration);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SimpleNotificationCenterNotifier other = (SimpleNotificationCenterNotifier) obj;
        return Objects.equal(this.configuration, other.configuration);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("configuration", configuration)
            .toString();
    }
}
