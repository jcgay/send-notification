package fr.jcgay.notification;

import com.google.auto.value.AutoValue;

/**
 * The application we want to send notification(s) from.
 */
@AutoValue
public abstract class Application {

    /**
     * Uniquely identify an application. <br>
     * In order to ensure your application's signature is unique, the recommendation is to follow the Internet Media
     * type (also known as MIME content type) format as defined in IETF <a href="http://tools.ietf.org/html/rfc2046">RFC 2046</a>
     * which contains the application vendor's name - specifically: {@code application/x-vnd-some_vendor.some_app}. <br>
     * The signature must not contain spaces. <br>
     * Some examples of acceptable signatures:
     * <ul>
     *     <li>{@code application/x-vnd-acme.hello_world}</li>
     *     <li>{@code application/x-vnd-fullphat.snaRSS}</li>
     * </ul>
     *
     * @return unique identifier.
     */
    public abstract String id();

    /**
     * A name that will identify the application for the user. <br>
     * Examples:
     * <ul>
     *     <li>Maven</li>
     *     <li>Gradle</li>
     * </ul>
     *
     * @return application name.
     */
    public abstract String name();

    /**
     * A timeout to wait for before cancelling application registration or notification sending.
     *
     * @return timeout in milliseconds, or {@code -1} if not specified.
     */
    public abstract long timeout();

    /**
     * The application icon.
     *
     * @return application icon.
     */
    public abstract Icon icon();

    Application() {
        // prevent external subclasses
    }

    public static Builder builder() {
        return new AutoValue_Application.Builder()
            .timeout(-1);
    }

    public static Builder builder(String id, String name, Icon icon) {
        return builder()
            .id(id)
            .name(name)
            .icon(icon);
    }

    @AutoValue.Builder
    public interface Builder {
        Builder id(String id);
        Builder name(String name);
        Builder icon(Icon icon);
        Builder timeout(long timeout);
        Application build();
    }
}
