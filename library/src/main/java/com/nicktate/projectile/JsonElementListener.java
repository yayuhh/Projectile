package com.nicktate.projectile;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Convenience listener that returns a {@link com.google.gson.JsonElement}
 */
public abstract class JsonElementListener implements ResponseListener<JsonElement> {
    @Override
    public JsonElement responseParser(NetworkResponse networkResponse) {
        String response;
        try {
            response = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            response = new String(networkResponse.data);
        }

        return new JsonParser().parse(response);
    }
}
