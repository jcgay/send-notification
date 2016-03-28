#send-notification

A toolbox in `Java` to send notifications.

## Usage

### Add dependency with your favorite build tool.

Example with *Maven*:

```xml
    <dependencies>
     [...]
        <dependency>
            <groupId>fr.jcgay.send-notification</groupId>
            <artifactId>send-notification</artifactId>
            <version>0.10</version>
        </dependency>
     [...]
    </dependencies>
```

Get a notifier:

```java
    Notifier notifier = new SendNotification()
        .setApplication(application)
        .initNotifier();
```

Then send notification:

```java
    try {
        notifier.send(notification);
    } finally {
        notifier.close;
    }
```    

## CLI

Download [bundle](https://bintray.com/artifact/download/jcgay/maven/fr/jcgay/send-notification/send-notification-cli/0.10/send-notification-cli-0.10-binaries.zip), extract it, add the `bin` directory to your `$PATH`.

    > send-notification -h
    
    Usage: <main class> [options] notifier(s)
      Options:
        -h, --help
           show help
           Default: false
      * -i, --icon
           notification icon
        -l, --level
           notification level (INFO, WARNING, ERROR)
      * -m, --message
           notification message
        -s, --subtitle
           notification subtitle
      * -t, --title
           notification title
        -v, --version
           show version
           Default: false

(*) are mandatory parameters.

Example:

    send-notification notifysend -m "Build success !" -t "maven-notifier" -i "success.png"
    
# Available notifiers

Go to [Wiki](https://github.com/jcgay/send-notification/wiki) to read full configuration guide for each notifier.

| Notifier | Screenshot |
|:--------:|-----------------|
| **Growl**, for [Windows](http://www.growlforwindows.com/gfw/) and [OS X](http://growl.info/).    | ![Growl](http://jeanchristophegay.com/images/notifier.growl_.success.png) |
| **[Snarl](http://snarl.fullphat.net/)**, for Windows | ![Snarl](http://jeanchristophegay.com/images/notifier.snarl.success.png) |
| **[terminal-notifier](https://github.com/alloy/terminal-notifier)**, OS X | ![terminal-notifier](http://jeanchristophegay.com/images/notifier.notification-center.success.png) |
| **notification center** OS X (since Mavericks) | ![notification-center](http://jeanchristophegay.com/images/notifier.simplenc.thumbnail.png) |
| **notify-send** for Linux | ![notify-send](http://jeanchristophegay.com/images/notifier.notify-send.success.png) |
| **SystemTray** since Java 6 | ![System Tray](http://jeanchristophegay.com/images/notifier.system.tray_.success.png) |
| **[Pushbullet](https://www.pushbullet.com/)** | ![pushbullet](http://jeanchristophegay.com/images/notifier.pushbullet.success.png) |
| **Kdialog** for KDE | ![Kdialog](http://jeanchristophegay.com/images/notifier.kdialog.png) |
| **[notifu](http://www.paralint.com/projects/notifu/index.html)** for Windows | ![notifu](http://jeanchristophegay.com/images/notifier.notifu.png) |
| **AnyBar** for [OS X](https://github.com/tonsky/AnyBar) and [Linux](https://github.com/limpbrains/somebar) | ![anybar](http://jeanchristophegay.com/images/notifier.anybar_maven.png) |
| **[Toaster](https://github.com/nels-o/toaster)** for Windows 8 | ![toaster](http://jeanchristophegay.com/images/notifier.toaster.png) |

# Build status

[![Build Status](https://travis-ci.org/jcgay/send-notification.svg?branch=master)](https://travis-ci.org/jcgay/send-notification)
[![Coverage Status](https://coveralls.io/repos/jcgay/send-notification/badge.svg?branch=master)](https://coveralls.io/r/jcgay/send-notification?branch=master)

# Release

    mvn -B release:prepare release:perform
    