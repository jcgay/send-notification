package fr.jcgay.notification.notifier.kdialog;

import com.google.common.base.Joiner;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.notifier.executor.Executor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class KdialogNotifier implements Notifier {

    private static final Logger LOGGER = getLogger(KdialogNotifier.class);

    private final Application application;
    private final KdialogConfiguration configuration;
    private final Executor executor;

    public KdialogNotifier(Application application, KdialogConfiguration configuration, Executor executor) {
        LOGGER.debug("Configuring notifu for application {}: {}.", application, configuration);
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
        commands.add("--passivepopup");
        commands.add(notification.message());
        if (application.timeout() != -1) {
            commands.add(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(application.timeout())));
        }
        commands.add("--title");
        commands.add(notification.title());
        commands.add("--icon");
        commands.add(notification.icon().asPath());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Will execute command line: " + Joiner.on(" ").join(commands));
        }

        try {
            executor.exec(commands.toArray(new String[commands.size()]));
        } catch (RuntimeException e) {
            throw new KdialogException("Error while sending notification with Kdialog.", e.getCause());
        }
    }

    @Override
    public void close() {
        // do nothing
    }
}
