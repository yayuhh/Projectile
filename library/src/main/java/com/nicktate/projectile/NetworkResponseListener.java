package com.nicktate.projectile;

import com.android.volley.NetworkResponse;

/**
 * Convenience listener that returns a {@link com.android.volley.NetworkResponse}
 */
public abstract class NetworkResponseListener implements ResponseListener<NetworkResponse> {
    @Override
    public NetworkResponse responseParser(NetworkResponse networkResponse) {
        return networkResponse;
    }
}
