package fr.jcgay.notification.executor

import fr.jcgay.notification.notifier.executor.Executor


class FakeExecutor implements Executor {

    List<String> executedCommand = []

    @Override
    void exec(String[] command) {
        executedCommand = command
    }

    @Override
    boolean tryExec(String[] command) {
        throw new IllegalStateException("This method should not be called!")
    }

    @Override
    void close() {}
}
