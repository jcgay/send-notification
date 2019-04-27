package fr.jcgay.notification.configuration;

public class OperatingSystem {

    private final String currentOs;

    public OperatingSystem() {
        currentOs = System.getProperty("os.name").toLowerCase();
    }

    public OperatingSystem(String currentOs) {
        this.currentOs = currentOs.toLowerCase();
    }

    public boolean isMac() {
        return currentOs.contains("mac");
    }

    public boolean isWindows() {
        return currentOs.contains("win");
    }

    @Override
    public String toString() {
        return "OperatingSystem{" +
                "currentOs='" + currentOs + '\'' +
                '}';
    }
}
