package fr.jcgay.notification.notifier.toaster;

import com.google.common.base.Joiner;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.notifier.executor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ToasterNotifier implements Notifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToasterNotifier.class);
    private static final String DOUBLE_QUOTE = "\"";

    private final ToasterConfiguration configuration;
    private final Executor executor;

    public ToasterNotifier(ToasterConfiguration configuration, Executor executor) {
        LOGGER.debug("Configuring toaster: {}.", configuration);
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
        commands.add("-t");
        commands.add(DOUBLE_QUOTE + notification.title() + DOUBLE_QUOTE);
        commands.add("-m");
        commands.add(DOUBLE_QUOTE + notification.message() + DOUBLE_QUOTE);
        commands.add("-p");
        commands.add(DOUBLE_QUOTE + notification.icon().asPath() + DOUBLE_QUOTE);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Will execute command line: " + Joiner.on(" ").join(commands));
        }

        try {
            executor.exec(commands.toArray(new String[commands.size()]));
        } catch (RuntimeException e) {
            throw new ToasterNotificationException("Error while sending notification with toaster", e);
        }
    }

    @Override
    public void close() {
        // do nothing
    }
}
