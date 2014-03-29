package com.nicktate.projectile;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class used to interface with {@link com.android.volley.toolbox.Volley} requests via the builder paradigm.
 */
public class Projectile {
    private static final Object sLock = new Object();
    private static Projectile sInstance;

    // global configs
    private static RequestQueue sRequestQueue;
    private static boolean sUseOkHttp = true;
    private static String sBaseUrl;

    /**
     * Cancel requests based on {@link com.android.volley.RequestQueue.RequestFilter}
     *
     * @param filter
     */
    public void cancelRequests(RequestQueue.RequestFilter filter) {
        sRequestQueue.cancelAll(filter);
    }

    /**
     * Cancel all outgoing requests
     */
    public void cancelAllRequests() {
        cancelRequests(
            new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            }
        );
    }

    public RequestBuilder aim(String url) {
        return new RequestBuilder(this, addBaseUrl(sBaseUrl,url));
    }

    public class RequestBuilder {
        private final Projectile mProjectile;
        private String mUrl;
        private Method mMethod = Method.GET;
        private Priority mPriority = Priority.NORMAL;

        private RetryPolicy mRetryPolicy;
        private int mNetworkTimeout = 10000; // 10 seconds
        private int mRetryCount = 3;
        private float mBackoffMultiplier = 1f;

        private Map<String, String> mHeaders = new HashMap<>();
        private Map<String, String> mParams = new HashMap<>();

        public RequestBuilder(Projectile projectile, String url) {
            mProjectile = projectile;
            mUrl = url;
        }

        public RequestBuilder method(Method method) {
            mMethod = method;
            return this;
        }

        public RequestBuilder priority(Priority priority) {
            mPriority = priority;
            return this;
        }

        public RequestBuilder addHeaders(Map<String, String> headers) {
            if (headers != null) mHeaders.putAll(headers);
            return this;
        }

        public RequestBuilder addHeader(String key, String value) {
            if (key != null && value != null) mHeaders.put(key, value);
            return this;
        }

        public RequestBuilder addParams(Map<String, String> params) {
            if (params != null) mParams.putAll(params);
            return this;
        }

        public RequestBuilder addParam(String key, String value) {
            if (key != null && value != null) mParams.put(key, value);
            return this;
        }

        public RequestBuilder retryPolicy(RetryPolicy policy) {
            mRetryPolicy = policy;
            return this;
        }

        public RequestBuilder timeout(int timeoutMillis) {
            mNetworkTimeout = timeoutMillis;
            return this;
        }

        public RequestBuilder retryCount(int retryCount) {
            mRetryCount = retryCount;
            return this;
        }

        public RequestBuilder backoffMultiplier(float backoff) {
            mBackoffMultiplier = backoff;
            return this;
        }

        public <T> void fire(ResponseListener<T> listener) {
            if (listener == null)
                throw new IllegalStateException("You must set a response listener before you fire your request!");
            if (mUrl == null)
                throw new IllegalStateException("Your target url cannot be null for the request");


            // volley only uses the getParams() method in PUT or POST requests, so in-case of GET or DELETE,
            // we must manually add them to query parameters
            // todo: look into RFC spec about other request types: https://tools.ietf.org/html/rfc2616
            if(mMethod == Method.GET || mMethod == Method.DELETE) {
                addQueryParams(mUrl, mParams);
            }

            // if custom retry policy was not provided, use DefaultRetryPolicy with configured params
            if(mRetryPolicy == null) {
                mRetryPolicy = new DefaultRetryPolicy(mNetworkTimeout, mRetryCount, mBackoffMultiplier);
            }

            mProjectile.sRequestQueue.add(new ProjectileRequest<> (
                    mMethod.getValue(),
                    mUrl,
                    mHeaders,
                    mParams,
                    listener,
                    mPriority.getValue(),
                    mRetryPolicy
            ));
        }
    }

    /**
     * Takes map of params and adds them to the URL, taking into account any existing query parameters.
     *
     * @param url
     * @param params
     * @return String appended with URL encoded query parameters.
     */
    private static String addQueryParams(String url, Map<String, String> params) {
        if(params == null || params.isEmpty()) return url;

        boolean isFirst = true;

        // re-adding exiting query parameters to ensure they are URL encoded
        if(url.contains("?")) {
            String[] split = url.split("\\?");
            url = split[0];
            String[] queryPairs = split[1].split("&");
            for(String tuple : queryPairs) {
                String[] queryParam = tuple.split("=");

                if(queryParam.length == 2) {
                    try {
                        url += isFirst ? "?" : "&";
                        url += queryParam[0] + "=" + URLEncoder.encode(URLDecoder.decode(queryParam[1], "utf-8"), "utf-8");
                        isFirst = false;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                // if split is length 1, then assume empty value
                else if(queryParam.length == 1) {
                    url += isFirst ? "?" : "&";
                    url += queryParam[0] + "=";
                    isFirst = false;
                }
            }
        }

        // add any passed in parameters to the query
        for(Map.Entry<String, String> entry : params.entrySet()) {
            try {
                url += isFirst ? "?" : "&";
                url += entry.getKey() + "=" + URLEncoder.encode(URLDecoder.decode(entry.getValue(), "utf-8"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            isFirst = false;
        }

        return url;
    }

    /**
     * Add a base url to the provided relative url
     *
     * @param baseUrl
     * @param relativeUrl
     * @return
     */
    private static String addBaseUrl(String baseUrl, String relativeUrl) {
        if(TextUtils.isEmpty(baseUrl)) { return relativeUrl; }
        if(TextUtils.isEmpty(relativeUrl)) { return baseUrl; }

        // network-path URL
        // todo: need to determine whether current schema versus just returning http
        if(relativeUrl.startsWith("//")) {
            return "http:" + relativeUrl;
        }

        String fullUrl;
        if(baseUrl.endsWith("/")) {
            if(relativeUrl.startsWith("/")) {
                fullUrl = baseUrl + relativeUrl.substring(1);
            } else {
                fullUrl = baseUrl + relativeUrl;
            }
        } else {
            if(relativeUrl.startsWith("/")) {
                fullUrl = baseUrl + relativeUrl;
            } else {
                fullUrl = baseUrl + "/" + relativeUrl;
            }
        }

        return fullUrl;
    }

    public static Projectile draw(Context ctx) {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new Builder(ctx).build();
            }
        }
        return sInstance;
    }

    /**
     * Set whether or not to use {@link com.squareup.okhttp.OkHttpClient} for url requests
     */
    public static void useOkHttp(boolean shouldUse) {
        sUseOkHttp = shouldUse;
    }

    /**
     * Set whether or not to use {@link com.squareup.okhttp.OkHttpClient} for url requests
     */
    public static void setBaseUrl(String baseUrl) {
        sBaseUrl = baseUrl;
    }

    /**
     * Set a custom queue to use for requests
     * @param queue
     */
    public static void setRequestQueue(RequestQueue queue) {
        sRequestQueue = queue;
    }

    private static class Builder {
        private Context ctx;

        public Builder(Context ctx) {
            if (ctx == null) throw new IllegalArgumentException("Context must not be null!");
            this.ctx = ctx.getApplicationContext();
        }

        public Projectile build() {
            Context context = ctx;

            return new Projectile(context);
        }
    }

    private Projectile(Context ctx) {
        if(sRequestQueue == null) sRequestQueue = Volley.newRequestQueue(ctx, sUseOkHttp ? new OkHttpStack() : new HurlStack());
    }
}