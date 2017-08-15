package fr.jcgay.notification.notifier.notifu;

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

public class NotifuNotifier implements DiscoverableNotifier {

    private static final Logger LOGGER = getLogger(NotifuNotifier.class);

    private final Application application;
    private final NotifuConfiguration configuration;
    private final Executor executor;

    public NotifuNotifier(Application application, NotifuConfiguration configuration, Executor executor) {
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
        commands.add("/p");
        commands.add(escape(notification.title()));
        commands.add("/m");
        commands.add(escape(notification.message()));
        commands.add("/d");
        if (application.timeout() == -1) {
            commands.add(String.valueOf(TimeUnit.SECONDS.toMillis(10)));
        } else {
            commands.add(String.valueOf(application.timeout()));
        }
        commands.add("/t");
        commands.add(toType(notification.level()));
        commands.add("/q");

        try {
            executor.exec(commands.toArray(new String[commands.size()]));
        } catch (RuntimeException e) {
            throw new NotifuException("Error while sending notification to notifu.", e.getCause());
        }
    }

    private static String escape(String title) {
        return title.replace("\"", "\\\"");
    }

    private static String toType(Notification.Level level) {
        switch (level) {
            case WARNING:
                return "warn";
            case ERROR:
                return "error";
            default:
                return "info";
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
        commands.add("/v");

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
        final NotifuNotifier other = (NotifuNotifier) obj;
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
