package fr.jcgay.notification;

import fr.jcgay.notification.configuration.ChosenNotifiers;
import fr.jcgay.notification.configuration.OperatingSystem;
import fr.jcgay.notification.notifier.DoNothingNotifier;
import fr.jcgay.notification.notifier.additional.AdditionalNotifier;
import org.slf4j.Logger;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Collections.unmodifiableSet;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.toCollection;
import static org.slf4j.LoggerFactory.getLogger;

class NotifierProvider {

    private static final Logger LOGGER = getLogger(NotifierProvider.class);

    private final ServiceLoader<fr.jcgay.notification.spi.NotifierProvider> loader = ServiceLoader.load(fr.jcgay.notification.spi.NotifierProvider.class);

    private static final ChosenNotifiers NOTIFICATION_CENTER = ChosenNotifiers.from("notificationcenter");
    private static final ChosenNotifiers NOTIFY_SEND = ChosenNotifiers.from("notifysend");
    private static final ChosenNotifiers SNARL = ChosenNotifiers.from("snarl");
    private static final ChosenNotifiers SYSTEM_TRAY = ChosenNotifiers.from("systemtray");
    private static final ChosenNotifiers KDIALOG = ChosenNotifiers.from("kdialog");
    private static final ChosenNotifiers TOASTER = ChosenNotifiers.from("toaster");

    private final OperatingSystem os;

    public NotifierProvider(OperatingSystem os) {
        this.os = os;
    }

    public DiscoverableNotifier byName(ChosenNotifiers notifier, Properties properties, Application application) {

        if (!notifier.secondary().isEmpty()) {
            LinkedHashSet<DiscoverableNotifier> secondary = new LinkedHashSet<>(notifier.secondary().size());
            for (String secondaryNotifier : notifier.secondary()) {
                secondary.add(byName(ChosenNotifiers.from(secondaryNotifier), properties, application));
            }

            return new AdditionalNotifier(
                byName(ChosenNotifiers.from(notifier.primary()), properties, application),
                unmodifiableSet(secondary)
            );
        }

        return allNotifiers()
            .filter(e -> notifier.primary().equalsIgnoreCase(e.id()))
            .findFirst()
            .map(e -> e.create(application, properties))
            .orElseGet(() -> {
                LOGGER.warn("Your configured notifier [{}] does not exist. Visit https://github.com/jcgay/send-notification/wiki#configuration to select an existing notifier.", notifier);
                return DoNothingNotifier.doNothing();
            });
    }

    public Set<DiscoverableNotifier> available(Properties configuration, Application application) {
        if (os.isMac()) {
            return Stream.of(
                    byNameQuietly("growl", configuration, application),
                    byNameQuietly(NOTIFICATION_CENTER.primary(), configuration, application),
                    byNameQuietly(SYSTEM_TRAY.primary(), configuration, application))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toCollection(LinkedHashSet::new));
        }

        if (os.isWindows()) {
            return Stream.of(
                    byNameQuietly(SNARL.primary(), configuration, application),
                    byNameQuietly("growl", configuration, application),
                    byNameQuietly(TOASTER.primary(), configuration, application),
                    byNameQuietly(SYSTEM_TRAY.primary(), configuration, application))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toCollection(LinkedHashSet::new));

        }

        return Stream.of(
                byNameQuietly(KDIALOG.primary(), configuration, application),
                byNameQuietly(NOTIFY_SEND.primary(), configuration, application),
                byNameQuietly(SYSTEM_TRAY.primary(), configuration, application))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toCollection(LinkedHashSet::new));
    }

    private Optional<DiscoverableNotifier> byNameQuietly(String notifierId, Properties properties, Application application) {
        return allNotifiers()
            .filter(e -> notifierId.equalsIgnoreCase(e.id()))
            .findFirst()
            .map(e -> e.createQuietly(application, properties));
    }

    private Stream<fr.jcgay.notification.spi.NotifierProvider> allNotifiers() {
        return StreamSupport.stream(spliteratorUnknownSize(loader.iterator(), ORDERED), false);
    }
}
