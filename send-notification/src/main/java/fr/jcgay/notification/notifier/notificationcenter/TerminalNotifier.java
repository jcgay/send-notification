package fr.jcgay.notification.notifier.notificationcenter;

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

public class TerminalNotifier implements DiscoverableNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalNotifier.class);

    private static final String CMD_MESSAGE = "-message";
    private static final String CMD_TITLE = "-title";
    private static final String CMD_SUBTITLE = "-subtitle";
    private static final String CMD_GROUP = "-group";
    private static final String CMD_ACTIVATE = "-activate";
    private static final String CMD_CONTENT_IMAGE = "-contentImage";
    private static final String CMD_SOUND = "-sound";
    private static final String CMD_APP_ICON = "-appIcon";

    private final Application application;
    private final TerminalNotifierConfiguration configuration;
    private final Executor executor;

    public TerminalNotifier(Application application, TerminalNotifierConfiguration configuration, Executor executor) {
        LOGGER.debug("Configuring terminal-notifier for application {}: {}.", application, configuration);
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
        commands.add(CMD_TITLE);
        commands.add(application.name());
        if (notification.subtitle() != null) {
            commands.add(CMD_SUBTITLE);
            commands.add(notification.subtitle());
        }
        commands.add(CMD_MESSAGE);
        commands.add(notification.message());
        commands.add(CMD_GROUP);
        commands.add(application.id());
        commands.add(CMD_ACTIVATE);
        commands.add(configuration.activateApplication());
        commands.add(CMD_CONTENT_IMAGE);
        commands.add(notification.icon().asPath());
        if (configuration.sound() != null) {
            commands.add(CMD_SOUND);
            commands.add(configuration.sound());
        }
        commands.add(CMD_APP_ICON);
        commands.add(application.icon().asPath());

        try {
            executor.exec(commands.toArray(new String[commands.size()]));
        } catch (RuntimeException e) {
            throw new TerminalNotifierNotificationException("Error while sending notification to terminal-notifier", e);
        }
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public boolean tryInit() {
        List<String> commands = new ArrayList<String>();
        commands.add(configuration.bin());
        commands.add("-help");

        try {
            return executor.exec(commands.toArray(new String[commands.size()])).waitFor() == 0;
        } catch (RuntimeException e) {
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
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
        final TerminalNotifier other = (TerminalNotifier) obj;
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
