package com.nicktate.projectile;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <p>
 * An {@link com.android.volley.toolbox.HttpStack HttpStack} implementation which
 * uses OkHttp as its transport.
 * </p>
 *
 * @See https://gist.github.com/JakeWharton/5616899
 */
public class OkHttpStack extends HurlStack {
    private final OkHttpClient client;

    public OkHttpStack() {
        this(new OkHttpClient());
    }

    public OkHttpStack(OkHttpClient client) {
        if (client == null) {
            throw new NullPointerException("Client must not be null.");
        }
        this.client = client;
    }

    @Override protected HttpURLConnection createConnection(URL url) throws IOException {
        return client.open(url);
    }
}
