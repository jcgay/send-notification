package fr.jcgay.notification.notifier.executor;

import com.google.common.base.Throwables;

import java.io.IOException;

public class RuntimeExecutor implements Executor {

    @Override
    public Process exec(String[] command) {
        try {
            return Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
