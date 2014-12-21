package fr.jcgay.notification.cli;

import com.beust.jcommander.IStringConverter;
import fr.jcgay.notification.Notification;

public class NotificationLevelConverter implements IStringConverter<Notification.Level> {

    @Override
    public Notification.Level convert(String value) {
        return Notification.Level.valueOf(value.toUpperCase());
    }
}
