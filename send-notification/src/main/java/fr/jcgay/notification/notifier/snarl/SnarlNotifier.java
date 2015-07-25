package fr.jcgay.notification.notifier.snarl;

import com.google.common.base.Objects;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.snp4j.Icon;
import fr.jcgay.snp4j.Server;
import fr.jcgay.snp4j.SnpException;
import fr.jcgay.snp4j.impl.SnpNotifier;
import fr.jcgay.snp4j.request.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.io.Closeables.closeQuietly;

public class SnarlNotifier implements DiscoverableNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnarlNotifier.class);

    private final Application application;
    private final SnarlConfiguration configuration;

    private fr.jcgay.snp4j.Notifier snarl;

    public SnarlNotifier(Application application, SnarlConfiguration configuration) {
        LOGGER.debug("Configuring Snarl for application {}: {}.", application, configuration);
        this.application = application;
        this.configuration = configuration;
    }

    @Override
    public Notifier init() {
        if (snarl != null) {
            return this;
        }

        Server server = Server.builder()
            .withHost(configuration.host())
            .withPort(configuration.port())
            .build();

        try {
            snarl = SnpNotifier.of(buildSnarlApp(), server);
        } catch (SnpException e) {
            throw new SnarlNotificationException("Cannot register application with Snarl.", e);
        }
        return this;
    }

    private fr.jcgay.snp4j.Application buildSnarlApp() {
        return fr.jcgay.snp4j.Application.of(application.id(), application.name());
    }

    @Override
    public void send(Notification notification) {
        if (snarl == null) {
            LOGGER.warn("Snarl notifier is not initialized, cannot send notification.");
            return;
        }

        fr.jcgay.snp4j.request.Notification snarNotification = new fr.jcgay.snp4j.request.Notification();
        snarNotification.setIcon(Icon.base64(notification.icon().toByteArray()));
        snarNotification.setText(notification.message());
        snarNotification.setTitle(notification.title());
        snarNotification.setPriority(toPriority(notification.level()));

        try {
            snarl.send(snarNotification);
        } catch (SnpException e) {
            throw new SnarlNotificationException("Cannot send notification to Snarl.", e);
        }
    }

    @Override
    public void close() {
        if (snarl != null) {
            closeQuietly(snarl);
        }
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public boolean tryInit() {
        try {
            init();
            return true;
        } catch (RuntimeException e) {
            close();
            return false;
        }
    }

    private static Priority toPriority(Notification.Level level) {
        switch (level) {
            case WARNING:
            case ERROR:
                return Priority.HIGH;
            default:
                return Priority.NORMAL;
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
        final SnarlNotifier other = (SnarlNotifier) obj;
        return Objects.equal(this.application, other.application)
            && Objects.equal(this.configuration, other.configuration);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("application", application)
            .add("configuration", configuration)
            .toString();
    }
}
