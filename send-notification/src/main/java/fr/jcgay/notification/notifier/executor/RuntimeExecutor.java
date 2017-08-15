package fr.jcgay.notification.notifier.executor;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class RuntimeExecutor implements Executor {

    private static final Logger LOGGER = getLogger(RuntimeExecutor.class);

    @Override
    public Process exec(String[] command) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Will execute command line: " + Joiner.on(" ").join(command));
        }

        try {
            return Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public boolean tryExec(String[] command) {
        try {
            return exec(command).waitFor() == 0;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
