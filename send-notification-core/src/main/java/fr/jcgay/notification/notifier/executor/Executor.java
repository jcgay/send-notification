package fr.jcgay.notification.notifier.executor;

public interface Executor {
    void exec(String[] command);
    boolean tryExec(String[] command);
    void close();
}
