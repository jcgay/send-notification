package fr.jcgay.notification.notifier.snarl;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.snp4j.Icon;
import fr.jcgay.snp4j.Server;
import fr.jcgay.snp4j.SnpException;
import fr.jcgay.snp4j.impl.SnpNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.io.Closeables.closeQuietly;

public class SnarlNotifier implements Notifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnarlNotifier.class);

    private final Application application;
    private final SnarlConfiguration configuration;

    private fr.jcgay.snp4j.Notifier snarl;

    public SnarlNotifier(Application application, SnarlConfiguration configuration) {
        this.application = application;
        this.configuration = configuration;
    }

    @Override
    public void init() {
        Server server = Server.builder()
                .withHost(configuration.host())
                .withPort(configuration.port())
                .build();

        try {
            snarl = SnpNotifier.of(buildSnarlApp(), server);
        } catch (SnpException e) {
            throw new SnarlNotificationException("Cannot register application with Snarl.", e);
        }
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
}
