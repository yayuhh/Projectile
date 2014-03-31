package org.nicktate.projectile;

import com.android.volley.Request;

/**
 * Priority Method Type Mapping to {@link com.android.volley.Request.Method} for enum convenience in builder
 */
public enum Priority {
    LOW(Request.Priority.LOW),
    NORMAL(Request.Priority.NORMAL),
    HIGH(Request.Priority.HIGH),
    IMMEDIATE(Request.Priority.IMMEDIATE);

    private Request.Priority mPriority;

    public Request.Priority getValue() {
        return mPriority;
    }

    Priority(Request.Priority p) {
        mPriority = p;
    }
}
