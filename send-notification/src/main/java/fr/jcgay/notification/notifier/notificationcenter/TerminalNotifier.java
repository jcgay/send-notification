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
        commands.add("-title");
        commands.add(application.name());
        if (notification.subtitle() != null) {
            commands.add("-subtitle");
            commands.add(notification.subtitle());
        }
        commands.add("-message");
        commands.add(notification.message());
        commands.add("-group");
        commands.add(application.id());
        if (configuration.activateApplication() != null) {
            commands.add("-activate");
            commands.add(configuration.activateApplication());
        }
        commands.add("-contentImage");
        commands.add(notification.icon().asPath());
        if (configuration.sound() != null) {
            commands.add("-sound");
            commands.add(configuration.sound());
        }
        commands.add("-appIcon");
        commands.add(application.icon().asPath());
        if (application.timeout() != -1) {
            commands.add("-timeout");
            commands.add(String.valueOf(Math.round(application.timeout() / 1000)));
        }

        try {
            executor.exec(commands.toArray(new String[commands.size()]));
        } catch (RuntimeException e) {
            throw new TerminalNotifierNotificationException("Error while sending notification to terminal-notifier", e);
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
        commands.add("-help");

        return executor.tryExec(commands.toArray(new String[commands.size()]));
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
