package fr.jcgay.notification.notifier.pushbullet;

import com.squareup.mimecraft.FormEncoding;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import fr.jcgay.notification.Application;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Proxy;

public class PushbulletNotifier implements Notifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushbulletNotifier.class);

    private final PushbulletConfiguration configuration;

    private OkHttpClient client;

    public PushbulletNotifier(Application application, PushbulletConfiguration configuration) {
        LOGGER.debug("Configuring Pushbullet for application {}: {}.", application, configuration);
        this.configuration = configuration;
    }

    @Override
    public void init() {
        client = new OkHttpClient();
        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credentials = Credentials.basic(configuration.key(), "");
                return response.request().newBuilder().header("Authorization", credentials).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return authenticate(proxy, response);
            }
        });
    }

    @Override
    public void send(Notification notification) {
        Request request = new Request.Builder()
                .url("https://api.pushbullet.com/v2/pushes")
                .post(buildRequestBody(notification))
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                String message = String.format("Pushbullet notification has failed, [%s] - [%s]%n%s",
                        response.code(),
                        response.message(),
                        response.body().string()
                );
                LOGGER.error(message);
                throw new PushbulletNotificationException(message);
            }
        } catch (IOException e) {
            String message = "Error while sending pushbullet notification.";
            LOGGER.error(message, e);
            throw new PushbulletNotificationException(message, e);
        }
    }

    private RequestBody buildRequestBody(Notification notification) {
        FormEncoding.Builder builder = new FormEncoding.Builder();
        if (configuration.device() != null) {
            builder.add("device_iden", configuration.device());
        }
        builder.add("type", "note")
                .add("title", notification.title())
                .add("body", notification.message());

        ByteArrayOutputStream data;
        try {
            data = new ByteArrayOutputStream();
            builder.build().writeBodyTo(data);
        } catch (IOException e) {
            String message = "Can't build request body.";
            LOGGER.error(message, e);
            throw new PushbulletNotificationException(message, e);
        }

        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), data.toByteArray());
    }

    @Override
    public void close() {
        // do nothing
    }
}
