package org.nicktate.projectile;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * Request Type for interfacing with the Projectile helper
 */
public class ProjectileRequest<T> extends Request<T> {
    ResponseListener<T> mListener;
    Map<String, String> mHeaders, mParams;
    Priority mPriority = Priority.NORMAL;

    public ProjectileRequest(int method, String url, Map<String, String> headers, Map<String, String> params, final ResponseListener<T> responseListener, Priority p, RetryPolicy retryPolicy, Object tag, boolean shouldCache) {
        super(method, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(responseListener != null) responseListener.onError(volleyError);
            }
        });

        mListener = responseListener;
        mHeaders = headers;
        mParams = params;
        mPriority = p;
        setRetryPolicy(retryPolicy);
        setTag(tag);
        setShouldCache(shouldCache);
    }

    @Override protected void deliverResponse(T response) {
        if(mListener != null) mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return mParams != null ? mParams : super.getParams();
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(mListener != null ? mListener.responseParser(networkResponse) : null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
