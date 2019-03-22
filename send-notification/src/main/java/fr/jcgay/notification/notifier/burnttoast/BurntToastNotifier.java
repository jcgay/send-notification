package fr.jcgay.notification.notifier.burnttoast;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class BurntToastNotifier implements DiscoverableNotifier {

    private static final Logger LOGGER = getLogger(BurntToastNotifier.class);

    private final Application application;
    private final BurntToastNotifierConfiguration configuration;

    public BurntToastNotifier(Application application, BurntToastNotifierConfiguration configuration) {
        this.application = application;
        this.configuration = configuration;
    }

    @Override
    public void send(Notification notification) {
        StringBuilder command = new StringBuilder()
            .append("New-BurntToastNotification -Text '")
            .append(notification.title())
            .append("', '")
            .append(notification.message())
            .append("' -AppLogo ")
            .append(notification.icon().asPath());

        if (configuration.sound() == null) {
            command.append(" -Silent");
        } else {
            command.append(" -Sound ")
                .append(configuration.sound());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Will execute PowerShell: {}", command.toString());
        }

        PowerShellResponse response = PowerShell.executeSingleCommand(command.toString());
        LOGGER.debug("Response: {}", response.getCommandOutput());
        if (response.isError()) {
            throw new BurntToastException(response.getCommandOutput());
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
    public Notifier init() {
        return this;
    }

    @Override
    public boolean tryInit() {
        PowerShellResponse response = PowerShell.executeSingleCommand("Get-Module -ListAvailable | Format-Table Name");
        return !response.isError() && response.getCommandOutput().toLowerCase().contains("burnttoast");
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
        final BurntToastNotifier other = (BurntToastNotifier) obj;
        return Objects.equal(this.application, other.application)
            && Objects.equal(this.configuration, other.configuration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("application", application)
            .add("configuration", configuration)
            .toString();
    }
}
