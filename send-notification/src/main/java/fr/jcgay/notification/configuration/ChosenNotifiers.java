package fr.jcgay.notification.configuration;

import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

@AutoValue
public abstract class ChosenNotifiers {

    private static final String SEPARATOR = ",";

    public abstract String primary();

    public abstract Set<String> secondary();

    ChosenNotifiers() {
        // prevent external subclasses
    }

    public static ChosenNotifiers from(String notifier) {
        checkNotNull(notifier);

        if (!notifier.contains(SEPARATOR)) {
            return new AutoValue_ChosenNotifiers(notifier, Collections.<String>emptySet());
        }

        String[] notifiers = notifier.split(SEPARATOR);
        LinkedHashSet<String> secondary = new LinkedHashSet<String>();
        for (int i = 1; i < notifiers.length; i++) {
            secondary.add(notifiers[i]);
        }
        return new AutoValue_ChosenNotifiers(notifiers[0], secondary);
    }
}
