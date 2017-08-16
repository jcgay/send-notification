package fr.jcgay.notification;

import fr.jcgay.notification.configuration.ChosenNotifiers;
import fr.jcgay.notification.configuration.OperatingSystem;
import fr.jcgay.notification.notifier.DoNothingNotifier;
import fr.jcgay.notification.notifier.additional.AdditionalNotifier;
import fr.jcgay.notification.notifier.anybar.AnyBarConfiguration;
import fr.jcgay.notification.notifier.anybar.AnyBarNotifier;
import fr.jcgay.notification.notifier.burnttoast.BurntToastNotifier;
import fr.jcgay.notification.notifier.burnttoast.BurntToastNotifierConfiguration;
import fr.jcgay.notification.notifier.executor.RuntimeExecutor;
import fr.jcgay.notification.notifier.growl.GrowlConfiguration;
import fr.jcgay.notification.notifier.growl.GrowlNotifier;
import fr.jcgay.notification.notifier.kdialog.KdialogConfiguration;
import fr.jcgay.notification.notifier.kdialog.KdialogNotifier;
import fr.jcgay.notification.notifier.notificationcenter.SimpleNotificationCenterNotifier;
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifier;
import fr.jcgay.notification.notifier.notificationcenter.TerminalNotifierConfiguration;
import fr.jcgay.notification.notifier.notifu.NotifuConfiguration;
import fr.jcgay.notification.notifier.notifu.NotifuNotifier;
import fr.jcgay.notification.notifier.notify.NotifyConfiguration;
import fr.jcgay.notification.notifier.notify.NotifyNotifier;
import fr.jcgay.notification.notifier.notifysend.NotifySendConfiguration;
import fr.jcgay.notification.notifier.notifysend.NotifySendNotifier;
import fr.jcgay.notification.notifier.pushbullet.PushbulletConfiguration;
import fr.jcgay.notification.notifier.pushbullet.PushbulletNotifier;
import fr.jcgay.notification.notifier.slack.SlackConfiguration;
import fr.jcgay.notification.notifier.slack.SlackNotifier;
import fr.jcgay.notification.notifier.snarl.SnarlConfiguration;
import fr.jcgay.notification.notifier.snarl.SnarlNotifier;
import fr.jcgay.notification.notifier.systemtray.SystemTrayNotifier;
import fr.jcgay.notification.notifier.toaster.ToasterConfiguration;
import fr.jcgay.notification.notifier.toaster.ToasterNotifier;
import org.slf4j.Logger;

import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import static fr.jcgay.notification.notifier.growl.GntpSlf4jListener.DEBUG;
import static fr.jcgay.notification.notifier.growl.GntpSlf4jListener.ERROR;
import static java.util.Collections.unmodifiableSet;
import static org.slf4j.LoggerFactory.getLogger;

class NotifierProvider {

    private static final Logger LOGGER = getLogger(NotifierProvider.class);

    private static final ChosenNotifiers GROWL = ChosenNotifiers.from("growl");
    private static final ChosenNotifiers NOTIFICATION_CENTER = ChosenNotifiers.from("notificationcenter");
    private static final ChosenNotifiers NOTIFY_SEND = ChosenNotifiers.from("notifysend");
    private static final ChosenNotifiers PUSHBULLET = ChosenNotifiers.from("pushbullet");
    private static final ChosenNotifiers SNARL = ChosenNotifiers.from("snarl");
    private static final ChosenNotifiers SYSTEM_TRAY = ChosenNotifiers.from("systemtray");
    private static final ChosenNotifiers NOTIFU = ChosenNotifiers.from("notifu");
    private static final ChosenNotifiers KDIALOG = ChosenNotifiers.from("kdialog");
    private static final ChosenNotifiers ANYBAR = ChosenNotifiers.from("anybar");
    private static final ChosenNotifiers SIMPLE_NOTIFICATION_CENTER = ChosenNotifiers.from("simplenc");
    private static final ChosenNotifiers TOASTER = ChosenNotifiers.from("toaster");
    private static final ChosenNotifiers NOTIFY = ChosenNotifiers.from("notify");
    private static final ChosenNotifiers BURNT_TOAST = ChosenNotifiers.from("burnttoast");
    private static final ChosenNotifiers SLACK = ChosenNotifiers.from("slack");

    private final OperatingSystem os;

    public NotifierProvider(OperatingSystem os) {
        this.os = os;
    }

    public DiscoverableNotifier byName(ChosenNotifiers notifier, Properties properties, Application application) {

        if (!notifier.secondary().isEmpty()) {
            LinkedHashSet<DiscoverableNotifier> secondary = new LinkedHashSet<DiscoverableNotifier>(notifier.secondary().size());
            for (String secondaryNotifier : notifier.secondary()) {
                secondary.add(byName(ChosenNotifiers.from(secondaryNotifier), properties, application));
            }

            return new AdditionalNotifier(
                byName(ChosenNotifiers.from(notifier.primary()), properties, application),
                unmodifiableSet(secondary)
            );
        }

        if (GROWL.equals(notifier)) {
            return new GrowlNotifier(application, GrowlConfiguration.create(properties), ERROR);
        }
        if (NOTIFICATION_CENTER.equals(notifier)) {
            return new TerminalNotifier(application, TerminalNotifierConfiguration.create(properties), new RuntimeExecutor());
        }
        if (NOTIFY_SEND.equals(notifier)) {
            return new NotifySendNotifier(application, NotifySendConfiguration.create(properties), new RuntimeExecutor());
        }
        if (PUSHBULLET.equals(notifier)) {
            return new PushbulletNotifier(application, PushbulletConfiguration.create(properties));
        }
        if (SNARL.equals(notifier)) {
            return new SnarlNotifier(application, SnarlConfiguration.create(properties));
        }
        if (SYSTEM_TRAY.equals(notifier)) {
            return new SystemTrayNotifier(application);
        }
        if (NOTIFU.equals(notifier)) {
            return new NotifuNotifier(application, NotifuConfiguration.create(properties), new RuntimeExecutor());
        }
        if (KDIALOG.equals(notifier)) {
            return new KdialogNotifier(application, KdialogConfiguration.create(properties), new RuntimeExecutor());
        }
        if (ANYBAR.equals(notifier)) {
            return AnyBarNotifier.create(application, AnyBarConfiguration.create(properties));
        }
        if (SIMPLE_NOTIFICATION_CENTER.equals(notifier)) {
            return new SimpleNotificationCenterNotifier(TerminalNotifierConfiguration.create(properties), new RuntimeExecutor());
        }
        if (TOASTER.equals(notifier)) {
            return new ToasterNotifier(ToasterConfiguration.create(properties), new RuntimeExecutor());
        }
        if (NOTIFY.equals(notifier)) {
            return new NotifyNotifier(application, NotifyConfiguration.create(properties));
        }
        if (BURNT_TOAST.equals(notifier)) {
            return new BurntToastNotifier(application, BurntToastNotifierConfiguration.create(properties));
        }
        if (SLACK.equals(notifier)) {
            return new SlackNotifier(application, SlackConfiguration.create(properties));
        }

        LOGGER.warn("Your configured notifier [{}] does not exist. Visit https://github.com/jcgay/send-notification/wiki#configuration to select an existing notifier.", notifier);
        return DoNothingNotifier.doNothing();
    }

    public Set<DiscoverableNotifier> available(Properties configuration, Application application) {
        if (os.isMac()) {
            Set<DiscoverableNotifier> macNotifiers = new LinkedHashSet<DiscoverableNotifier>();
            macNotifiers.add(new GrowlNotifier(application, GrowlConfiguration.create(configuration), DEBUG));
            macNotifiers.add(byName(NOTIFICATION_CENTER, configuration, application));
            macNotifiers.add(byName(SYSTEM_TRAY,  configuration, application));
            return unmodifiableSet(macNotifiers);
        }

        if (os.isWindows()) {
            Set<DiscoverableNotifier> winNotifiers = new LinkedHashSet<DiscoverableNotifier>();
            winNotifiers.add(byName(SNARL, configuration, application));
            winNotifiers.add(new GrowlNotifier(application, GrowlConfiguration.create(configuration), DEBUG));
            winNotifiers.add(byName(TOASTER, configuration, application));
            winNotifiers.add(byName(BURNT_TOAST, configuration, application));
            winNotifiers.add(byName(SYSTEM_TRAY,  configuration, application));
            return unmodifiableSet(winNotifiers);
        }

        Set<DiscoverableNotifier> linuxNotifiers = new LinkedHashSet<DiscoverableNotifier>();
        linuxNotifiers.add(byName(KDIALOG, configuration, application));
        linuxNotifiers.add(byName(NOTIFY_SEND, configuration, application));
        linuxNotifiers.add(byName(SYSTEM_TRAY,  configuration, application));
        return unmodifiableSet(linuxNotifiers);
    }
}
