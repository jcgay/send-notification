package fr.jcgay.notification.notifier.executor;

import com.google.common.base.Throwables;

import java.io.IOException;

public class RuntimeExecutor implements Executor {

    @Override
    public void exec(String[] command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }
}
