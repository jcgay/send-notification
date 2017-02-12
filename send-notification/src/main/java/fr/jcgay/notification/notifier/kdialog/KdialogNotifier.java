package fr.jcgay.notification.notifier.kdialog;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.notifier.executor.Executor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class KdialogNotifier implements DiscoverableNotifier {

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
    public Notifier init() {
        return this;
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

        try {
            executor.exec(commands.toArray(new String[commands.size()]));
        } catch (RuntimeException e) {
            throw new KdialogException("Error while sending notification with Kdialog.", e);
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
        commands.add("-v");

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
        final KdialogNotifier other = (KdialogNotifier) obj;
        return Objects.equal(this.application, other.application)
            && Objects.equal(this.configuration, other.configuration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("configuration", configuration)
            .add("application", application)
            .toString();
    }
}
