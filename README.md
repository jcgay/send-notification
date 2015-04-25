#send-notification

A toolbox in `Java` to send notifications.

## Usage

### Add dependency with your favorite build tool.

Example with *Maven*:

    <dependencies>
     [...]
        <dependency>
            <groupId>fr.jcgay.send-notification</groupId>
            <artifactId>send-notification</artifactId>
            <version>0.3</version>
        </dependency>
     [...]
    </dependencies>

Get a notifier:

    Notifier notifier = new SendNotification()
        .setApplication(application)
        .chooseNotifier();

Then send notification:

    notifier.init();
    try {
        notifier.send(notification);
    } finally {
        notifier.close;
    }

## CLI

Download [bundle](http://search.maven.org/remotecontent?filepath=fr/jcgay/send-notification/send-notification-cli/0.3/send-notification-cli-0.3-binaries.zip), extract it, add the `bin` directory to your `$PATH`.

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

##Growl

For OS X [(paid app)](http://growl.info/) and Windows [(free)](http://www.growlforwindows.com/gfw/).

![Growl](http://jeanchristophegay.com/images/notifier.growl_.success.png)

##notify-send

For linux. 

![notify-send](http://jeanchristophegay.com/images/notifier.notify-send.success.png)

##Notification center

For OS X (at least Mountain lion) with [terminal-notifier](https://github.com/alloy/terminal-notifier).

![terminal-notifier](http://jeanchristophegay.com/images/notifier.notification-center.success.png)

##System tray

Use Java `SystemTray` to display notification.

![System Tray](http://jeanchristophegay.com/images/notifier.system.tray_.success.png)

##Snarl

For Windows [Snarl](http://snarl.fullphat.net/).

![Snarl](http://jeanchristophegay.com/images/notifier.snarl.success.png)

##Pushbullet

Use [Pushbullet](https://www.pushbullet.com/) online service.

![pushbullet](http://jeanchristophegay.com/images/notifier.pushbullet.success.png)

## Kdialog

For KDE.

![Kdialog](http://jeanchristophegay.com/images/notifier.kdialog.png)

## notifu

For Windows [notifu](http://www.paralint.com/projects/notifu/index.html).

![notifu](http://jeanchristophegay.com/images/notifier.notifu.png)

## AnyBar

For OS X, [AnyBar](https://github.com/tonsky/AnyBar).

![anybar](http://jeanchristophegay.com/images/notifier.anybar.png)

# Build status

[![Build Status](https://travis-ci.org/jcgay/send-notification.svg?branch=master)](https://travis-ci.org/jcgay/send-notification)
[![Coverage Status](https://coveralls.io/repos/jcgay/send-notification/badge.svg?branch=master)](https://coveralls.io/r/jcgay/send-notification?branch=master)
