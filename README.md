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
            <version>0.1</version>
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

Download [bundle](), extract it, add the `bin` directory to your `$PATH`.

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

![Growl success](http://jeanchristophegay.com/images/notifier.growl_.success.png)  ![Growl fail](http://jeanchristophegay.com/images/notifier.growl_.fail_.png)

##notify-send

For linux. 

![notify-send success](http://jeanchristophegay.com/images/notifier.notify-send.success.png)  
![notify-send fail](http://jeanchristophegay.com/images/notifier.notify-send.error_.fail_.png)

##Notification center

For OS X (at least Mountain lion) with [terminal-notifier](https://github.com/alloy/terminal-notifier).

![terminal-notifier](http://jeanchristophegay.com/images/notifier.notification-center.success.png)  ![terminal-notifier fail](http://jeanchristophegay.com/images/notifier.notification-center.failure.png)

##System tray

Use Java `SystemTray` to display notification.

![System Tray success](http://jeanchristophegay.com/images/notifier.system.tray_.success.png)  ![System Tray fail](http://jeanchristophegay.com/images/notifier.system.tray_.fail_.png)

##Snarl

For Windows [Snarl](http://snarl.fullphat.net/).

![Snarl](http://jeanchristophegay.com/images/notifier.snarl.success.png)  ![Snarl fail](http://jeanchristophegay.com/images/notifier.snarl.failure.png)

##Pushbullet

Use [Pushbullet](https://www.pushbullet.com/) online service.

![pushbullet success](http://jeanchristophegay.com/images/notifier.pushbullet.success.png)
![pushbullet failure](http://jeanchristophegay.com/images/notifier.pushbullet.failure.png)