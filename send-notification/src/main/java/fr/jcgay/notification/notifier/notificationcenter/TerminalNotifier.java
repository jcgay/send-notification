package fr.jcgay.notification.notifier.notificationcenter;

import com.google.common.base.Joiner;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.notifier.executor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TerminalNotifier implements Notifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalNotifier.class);

    private static final String CMD_MESSAGE = "-message";
    private static final String CMD_TITLE = "-title";
    private static final String CMD_SUBTITLE = "-subtitle";
    private static final String CMD_GROUP = "-group";
    private static final String CMD_ACTIVATE = "-activate";
    private static final String CMD_CONTENT_IMAGE = "-contentImage";

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
    public void init() {
        // do nothing
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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Will execute command line: " + Joiner.on(" ").skipNulls().join(commands));
        }

        try {
            executor.exec(commands.toArray(new String[commands.size()]));
        } catch (RuntimeException e) {
            throw new TerminalNotifierNotificationException("Error while sending notification to terminal-notifier", e.getCause());
        }
    }

    @Override
    public void close() {
        // do nothing
    }
}
