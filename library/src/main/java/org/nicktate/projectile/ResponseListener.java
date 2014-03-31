package org.nicktate.projectile;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * Projectile Listener interface for used to proxy volley requests back.
 */
public interface ResponseListener<T> {
    public T responseParser(NetworkResponse response);
    public void onResponse(T response);
    public void onError(VolleyError error);
}
