package fr.jcgay.notification;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class MultipleSendNotificationException extends SendNotificationException {

    private List<Exception> errors;

    public MultipleSendNotificationException(List<Exception> errors) {
        super(messages(checkNotNull(errors)));
        this.errors = errors;
    }

    public List<Exception> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    private static String messages(List<Exception> errors) {
        Iterator<Exception> iterator = errors.iterator();
        StringBuilder messages = new StringBuilder(iterator.next().getMessage());
        while (iterator.hasNext()) {
            Exception error = iterator.next();
            if (error.getMessage() != null) {
                messages.append(String.format("%n%s", error.getMessage()));
            }
        }
        return messages.toString();
    }
}
