package fr.jcgay.notification.notifier.slack;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.DiscoverableNotifier;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.slf4j.LoggerFactory.getLogger;

public class SlackNotifier implements DiscoverableNotifier {

    private static final Logger LOGGER = getLogger(SlackNotifier.class);

    private final Application application;
    private final SlackConfiguration configuration;
    private final String url;

    private OkHttpClient client;

    public SlackNotifier(Application application, SlackConfiguration configuration, String url) {
        LOGGER.debug("Configuring Slack for application {}: {}.", application, configuration);
        this.application = application;
        this.configuration = configuration;
        this.url = url;
    }

    public SlackNotifier(Application application, SlackConfiguration configuration) {
        this(application, configuration, "https://slack.com/api/chat.postMessage");
    }

    @Override
    public Notifier init() {
        if (client != null) {
            return this;
        }

        client = new OkHttpClient();

        return this;
    }

    @Override
    public boolean tryInit() {
        return false;
    }

    @Override
    public void send(Notification notification) {
        StringBuilder builder = new StringBuilder(url)
            .append("?token=").append(configuration.token())
            .append("&channel=").append(encode(configuration.channel()))
            .append("&attachments=").append(encode(attachments(notification)));

        try {
            String requestUrl = builder.toString();
            LOGGER.debug("Calling Slack with parameters: {}", requestUrl);
            Response response = client.newCall(new Request.Builder().url(requestUrl).get().build()).execute();
            if (response.code() != 200) {
                String message = String.format("Slack notification has failed, [%s] - [%s]%n%s",
                    response.code(),
                    response.message(),
                    response.body().string()
                );
                LOGGER.error(message);
                throw new SlackNotificationException(message);
            }
        } catch (IOException e) {
            String message = "Error while sending Slack notification.";
            LOGGER.error(message, e);
            throw new SlackNotificationException(message, e);
        }
    }

    private String attachments(Notification notification) {
        return "[{" +
            "\"fallback\":\"" + notification.message() + "\"," +
            "\"color\":\"" + colorize(notification) + "\"," +
            "\"author_name\":\"" + application.name() + "\"," +
            "\"title\":\"" + notification.title() + "\"," +
            "\"text\":\"" + notification.message() + "\n" + notification.subtitle() + "\"," +
            "}]";
    }

    private static String colorize(Notification notification) {
        switch (notification.level()) {
            case INFO:
                return "good";
            case WARNING:
                return "warning";
            case ERROR:
                return "danger";
            default:
                return "warning";
        }
    }

    private static String encode(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new SlackNotificationException("Cannot encode URL parameter: " + string, e);
        }
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public boolean isPersistent() {
        return false;
    }
}
