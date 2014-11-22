package fr.jcgay.notification.notifier.growl;

import com.google.code.jgntp.GntpErrorStatus;
import com.google.code.jgntp.GntpListener;
import com.google.code.jgntp.GntpNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GntpSlf4jListener implements GntpListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GntpSlf4jListener.class);

    @Override
    public void onRegistrationSuccess() {
        LOGGER.debug("Client has been successfully registered by Growl");
    }

    @Override
    public void onNotificationSuccess(GntpNotification notification) {
        LOGGER.debug("Notification success: {}", notification);
    }

    @Override
    public void onClickCallback(GntpNotification notification) {

    }

    @Override
    public void onCloseCallback(GntpNotification notification) {

    }

    @Override
    public void onTimeoutCallback(GntpNotification notification) {

    }

    @Override
    public void onRegistrationError(GntpErrorStatus status, String description) {
        LOGGER.error("Growl registration has failed.\n {}: {}", status, description);
    }

    @Override
    public void onNotificationError(GntpNotification notification, GntpErrorStatus status, String description) {
        LOGGER.error("Growl notification has failed.\n {}: {}\n {}", status, description, notification);
    }

    @Override
    public void onCommunicationError(Throwable t) {
        LOGGER.error("Cannot communicate with Growl.", t);
    }
}
