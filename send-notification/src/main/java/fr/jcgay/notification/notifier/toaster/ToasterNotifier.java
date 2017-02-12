package fr.jcgay.notification.notifier.toaster;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.notifier.executor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ToasterNotifier implements DiscoverableNotifier {

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
    public Notifier init() {
        return this;
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

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public boolean tryInit() {
        List<String> commands = new ArrayList<String>();
        commands.add(configuration.bin());

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
        final ToasterNotifier other = (ToasterNotifier) obj;
        return Objects.equal(this.configuration, other.configuration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("configuration", configuration)
            .toString();
    }
}
