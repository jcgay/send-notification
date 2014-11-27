package fr.jcgay.notification.notifier.growl;

import com.google.code.jgntp.Gntp;
import com.google.code.jgntp.GntpApplicationInfo;
import com.google.code.jgntp.GntpClient;
import com.google.code.jgntp.GntpNotification;
import com.google.code.jgntp.GntpNotificationInfo;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class GrowlNotifier implements Notifier {

    private static final Logger LOGGER = getLogger(GrowlNotifier.class);

    private final Application application;
    private final GrowlConfiguration configuration;

    private GntpApplicationInfo gApplication;
    private GntpNotificationInfo gNotification;
    private GntpClient gClient;

    public GrowlNotifier(Application application, GrowlConfiguration configuration) {
        LOGGER.debug("Configuring Growl for application {}: {}.", application, configuration);
        this.application = application;
        this.configuration = configuration;
    }

    @Override
    public void init() {
        gApplication = Gntp.appInfo(application.name()).build();
        gNotification = Gntp.notificationInfo(gApplication, application.id())
                .icon(application.icon().toRenderedImage())
                .build();
        Gntp clientBuilder = Gntp.client(gApplication)
                .onPort(configuration.port())
                .forHost(configuration.host())
                .listener(new GntpSlf4jListener());
        if (configuration.password() != null) {
            clientBuilder.withPassword(configuration.password());
        }
        gClient = clientBuilder.build();
        gClient.register();
        try {
            gClient.waitRegistration();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void send(Notification notification) {
        if (isClientRegistered()) {
            GntpNotification success = Gntp.notification(gNotification, notification.title())
                    .text(notification.message())
                    .icon(notification.icon().toRenderedImage())
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

    private boolean isClientRegistered() {
        return gClient != null && gClient.isRegistered();
    }

}
