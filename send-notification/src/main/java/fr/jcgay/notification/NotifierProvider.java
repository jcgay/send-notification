package fr.jcgay.notification;

import fr.jcgay.notification.configuration.OperatingSystem;
import fr.jcgay.notification.notifier.DoNothingNotifier;
import fr.jcgay.notification.notifier.anybar.AnyBarConfiguration;
import fr.jcgay.notification.notifier.anybar.AnyBarNotifier;
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
import fr.jcgay.notification.notifier.notifysend.NotifySendConfiguration;
import fr.jcgay.notification.notifier.notifysend.NotifySendNotifier;
import fr.jcgay.notification.notifier.pushbullet.PushbulletConfiguration;
import fr.jcgay.notification.notifier.pushbullet.PushbulletNotifier;
import fr.jcgay.notification.notifier.snarl.SnarlConfiguration;
import fr.jcgay.notification.notifier.snarl.SnarlNotifier;
import fr.jcgay.notification.notifier.systemtray.SystemTrayNotifier;
import fr.jcgay.notification.notifier.toaster.ToasterConfiguration;
import fr.jcgay.notification.notifier.toaster.ToasterNotifier;

import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

class NotifierProvider {

    private static final String GROWL = "growl";
    private static final String NOTIFICATION_CENTER = "notificationcenter";
    private static final String NOTIFY_SEND = "notifysend";
    private static final String PUSHBULLET = "pushbullet";
    private static final String SNARL = "snarl";
    private static final String SYSTEM_TRAY = "systemtray";
    private static final String NOTIFU = "notifu";
    private static final String KDIALOG = "kdialog";
    private static final String ANYBAR = "anybar";
    private static final String SIMPLE_NOTIFICATION_CENTER = "simplenc";
    private static final String TOASTER = "toaster";

    private final RuntimeExecutor executor = new RuntimeExecutor();
    private final OperatingSystem os;

    public NotifierProvider(OperatingSystem os) {
        this.os = os;
    }

    public DiscoverableNotifier byName(String name, Properties properties, Application application) {
        if (GROWL.equalsIgnoreCase(name)) {
            return new GrowlNotifier(application, GrowlConfiguration.create(properties));
        } else {
            if (NOTIFICATION_CENTER.equals(name)) {
                return new TerminalNotifier(application, TerminalNotifierConfiguration.create(properties), executor);
            } else if (NOTIFY_SEND.equals(name)) {
                return new NotifySendNotifier(application, NotifySendConfiguration.create(properties), executor);
            } else if (PUSHBULLET.equals(name)) {
                return new PushbulletNotifier(application, PushbulletConfiguration.create(properties));
            } else if (SNARL.equals(name)) {
                return new SnarlNotifier(application, SnarlConfiguration.create(properties));
            } else if (SYSTEM_TRAY.equals(name)) {
                return new SystemTrayNotifier(application);
            } else if (NOTIFU.equals(name)) {
                return new NotifuNotifier(application, NotifuConfiguration.create(properties), executor);
            } else if (KDIALOG.equals(name)) {
                return new KdialogNotifier(application, KdialogConfiguration.create(properties), executor);
            } else if (ANYBAR.equals(name)) {
                return AnyBarNotifier.create(application, AnyBarConfiguration.create(properties));
            } else if (SIMPLE_NOTIFICATION_CENTER.equals(name)) {
                return new SimpleNotificationCenterNotifier(TerminalNotifierConfiguration.create(properties), executor);
            } else if (TOASTER.equals(name)) {
                return new ToasterNotifier(ToasterConfiguration.create(properties), executor);
            } else {
                return DoNothingNotifier.doNothing();
            }
        }
    }

    public Set<DiscoverableNotifier> available(Properties configuration, Application application) {
        if (os.isMac()) {
            Set<DiscoverableNotifier> macNotifiers = new LinkedHashSet<DiscoverableNotifier>();
            macNotifiers.add(byName(GROWL, configuration, application));
            macNotifiers.add(byName(NOTIFICATION_CENTER, configuration, application));
            macNotifiers.add(byName(SYSTEM_TRAY,  configuration, application));
            return unmodifiableSet(macNotifiers);
        }

        if (os.isWindows()) {
            Set<DiscoverableNotifier> winNotifiers = new LinkedHashSet<DiscoverableNotifier>();
            winNotifiers.add(byName(SNARL, configuration, application));
            winNotifiers.add(byName(GROWL, configuration, application));
            winNotifiers.add(byName(TOASTER, configuration, application));
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
