package fr.jcgay.notification.notifier.growl;

import com.google.code.jgntp.Gntp;
import com.google.code.jgntp.GntpApplicationInfo;
import com.google.code.jgntp.GntpClient;
import com.google.code.jgntp.GntpNotification;
import com.google.code.jgntp.GntpNotificationInfo;
import com.google.common.base.Objects;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static com.google.code.jgntp.GntpNotification.Priority.HIGH;
import static com.google.code.jgntp.GntpNotification.Priority.HIGHEST;
import static com.google.code.jgntp.GntpNotification.Priority.NORMAL;
import static org.slf4j.LoggerFactory.getLogger;

public class GrowlNotifier implements DiscoverableNotifier {

    private static final Logger LOGGER = getLogger(GrowlNotifier.class);

    private final Application application;
    private final GrowlConfiguration configuration;

    private GntpNotificationInfo gNotification;
    private GntpClient gClient;

    public GrowlNotifier(Application application, GrowlConfiguration configuration) {
        LOGGER.debug("Configuring Growl for application {}: {}.", application, configuration);
        this.application = application;
        this.configuration = configuration;
    }

    @Override
    public Notifier init() {
        if (isClientRegistered()) {
            return this;
        }

        GntpApplicationInfo gApplication = Gntp.appInfo(application.name()).build();
        gNotification = Gntp.notificationInfo(gApplication, application.id())
            .icon(application.icon().toRenderedImage())
            .build();
        Gntp clientBuilder = Gntp.client(gApplication)
            .onPort(configuration.port())
            .forHost(configuration.host())
            .withoutRetry()
            .listener(new GntpSlf4jListener());
        if (configuration.password() != null) {
            clientBuilder.withPassword(configuration.password());
        }
        gClient = clientBuilder.build();
        gClient.register();
        try {
            gClient.waitRegistration(1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return this;
    }

    @Override
    public void send(Notification notification) {
        if (isClientRegistered()) {
            GntpNotification success = Gntp.notification(gNotification, notification.title())
                    .text(notification.message())
                    .icon(notification.icon().toRenderedImage())
                    .priority(toPriority(notification.level()))
                    .build();
            try {
                gClient.notify(success, 5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void close() {
        if (isClientRegistered()) {
            try {
                // Seems that the client can be shutdown without having processed all the notifications...
                TimeUnit.SECONDS.sleep(1);
                gClient.shutdown(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public boolean tryInit() {
        init();
        boolean hasSucceeded = isClientRegistered();
        if (!hasSucceeded) {
            try {
                gClient.shutdown(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return hasSucceeded;
    }

    private static GntpNotification.Priority toPriority(Notification.Level level) {
        switch (level) {
            case INFO:
                return NORMAL;
            case WARNING:
                return HIGH;
            case ERROR:
                return HIGHEST;
            default:
                return NORMAL;
        }
    }

    private boolean isClientRegistered() {
        return gClient != null && gClient.isRegistered();
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
        final GrowlNotifier other = (GrowlNotifier) obj;
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
