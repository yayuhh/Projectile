package com.nicktate.projectile;

import com.android.volley.Request;

/**
 * Request Method Type Mapping to {@link com.android.volley.Request.Method} for enum convenience in builder
 */
public enum Method {
    DELETE(Request.Method.DELETE),
    DEPRECATED_GET_OR_POST(Request.Method.DEPRECATED_GET_OR_POST),
    GET(Request.Method.GET),
    HEAD(Request.Method.HEAD),
    OPTIONS(Request.Method.OPTIONS),
    PATCH(Request.Method.PATCH),
    POST(Request.Method.POST),
    PUT(Request.Method.PUT),
    TRACE(Request.Method.TRACE);

    private int mMethod;

    public int getValue() {
        return mMethod;
    }

    Method(int method) {
        mMethod = method;
    }
}
