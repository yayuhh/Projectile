package org.nicktate.projectile;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * Convenience listener that returns a string
 */
public abstract class StringListener implements ResponseListener<String> {
    @Override
    public String responseParser(NetworkResponse networkResponse) {
        String response;
        try {
            response = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            response = new String(networkResponse.data);
        }
        return response;
    }
}
