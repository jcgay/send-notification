package fr.jcgay.notification.notifier.anybar;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.IconFileWriter;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static org.slf4j.LoggerFactory.getLogger;

public class AnyBarNotifier implements DiscoverableNotifier {

    private static final Logger LOGGER = getLogger(AnyBarNotifier.class);

    private final Application application;
    private final AnyBarConfiguration configuration;
    private final IconFileWriter iconWriter;
    private final DatagramSocket socket;

    private boolean isInitialized;

    @VisibleForTesting
    AnyBarNotifier(Application application, AnyBarConfiguration configuration, DatagramSocket socket, IconFileWriter iconWriter) {
        LOGGER.debug("Configuring AnyBar for application {}: {}.", application, configuration);
        this.socket = socket;
        this.iconWriter = iconWriter;
        this.application = application;
        this.configuration = configuration;
    }

    public static AnyBarNotifier create(Application application, AnyBarConfiguration configuration) {
        try {
            return new AnyBarNotifier(application, configuration, new DatagramSocket(), new AnyBarIconWriter());
        } catch (SocketException e) {
            throw new AnyBarException("Error while initiating connection with AnyBar", e);
        }
    }

    @Override
    public Notifier init() {
        if (isInitialized) {
            return this;
        }

        try {
            if (application.timeout() > 0) {
                socket.setSoTimeout((int) application.timeout());
            }
        } catch (java.io.IOException e) {
            throw new AnyBarException("Error while setting socket timeout", e);
        }

        changeIcon(application.icon());

        isInitialized = true;
        return this;
    }

    @Override
    public void send(Notification notification) {
        changeIcon(notification.icon());
    }

    @Override
    public void close() {
        socket.close();
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public boolean tryInit() {
        return false;
    }

    private void changeIcon(Icon icon) {
        iconWriter.write(icon);

        byte[] data = icon.id().getBytes();
        try {
            socket.send(new DatagramPacket(data, data.length, InetAddress.getByName(configuration.host()), configuration.port()));
        } catch (IOException e) {
            throw new AnyBarException("Error while changing AnyBar icon", e);
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
        final AnyBarNotifier other = (AnyBarNotifier) obj;
        return Objects.equal(this.application, other.application)
            && Objects.equal(this.configuration, other.configuration);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("configuration", configuration)
            .add("application", application)
            .toString();
    }
}
