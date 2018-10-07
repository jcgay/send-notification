package fr.jcgay.notification.notifier.anybar;

import com.google.common.annotations.VisibleForTesting;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.IconFileWriter;
import fr.jcgay.notification.SendNotificationException;
import net.coobird.thumbnailator.Thumbnailator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AnyBarIconWriter implements IconFileWriter {

    private final String destination;

    @VisibleForTesting
    AnyBarIconWriter(String destination) {
        this.destination = destination.endsWith("/") ? destination : destination + '/';
    }

    public AnyBarIconWriter() {
        this(System.getProperty("user.home") + "/.AnyBar/");
    }

    @Override
    public void write(Icon icon) {
        for (Dimension dimension : Dimension.values()) {
            File resizedIcon = new File(destination + icon.id() + dimension.fileNameSuffix + "." + icon.extension());

            if (!resizedIcon.exists()) {
                new File(destination).mkdirs();

                try (InputStream input = icon.content().openStream(); FileOutputStream output = new FileOutputStream(resizedIcon)) {
                    Thumbnailator.createThumbnail(input, output, dimension.width, dimension.height);
                } catch (IOException e) {
                    throw new SendNotificationException("Can't write notification icon: " + resizedIcon.getPath(), e);
                }
            }
        }
    }

    private enum Dimension {
        CLASSIC(19, 19),
        RETINA(38, 38, "@2x");

        private final int width;
        private final int height;
        private final String fileNameSuffix;

        Dimension(int width, int height, String fileNameSuffix) {
            this.width = width;
            this.height = height;
            this.fileNameSuffix = fileNameSuffix;
        }

        Dimension(int width, int height) {
            this(width, height, "");
        }
    }
}
