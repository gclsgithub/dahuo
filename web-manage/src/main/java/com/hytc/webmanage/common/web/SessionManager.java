package com.hytc.webmanage.common.web;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.Assert;

public enum SessionManager {
    me;

    /** set */
    final public <T> void set(final HttpServletRequest request, final SessionKey key, final T clazz) {
        final HttpSession session = request.getSession(false);
        Assert.notNull(session, "session can not be null!");
        this.set(session, key, clazz);
    }

    final public <T> void set(final HttpSession session, final SessionKey key, final T value) {
        session.setAttribute(key.name(), value);
    }

    /** get */
    final public <T> T get(final HttpServletRequest request, final SessionKey key, final Class<T> clazz, final T defaultValue) {
        final HttpSession session = request.getSession(false);
        Assert.notNull(session, "session can not be null!");
        return this.get(session, key, clazz, defaultValue);
    }

    @SuppressWarnings("unchecked")
    final public <T> T get(final HttpSession session, final SessionKey key, final Class<T> value, final T defaultValue) {
        Object retVal = session.getAttribute(key.name());
        if (retVal == null) {
            return defaultValue;
        }
        return (T) retVal;
    }

    /** list */
    final public <T> List<T> list(final HttpServletRequest request, final SessionKey key, final Class<T> clazz, final boolean newListSize0) {
        final HttpSession session = request.getSession(false);
        Assert.notNull(session, "session can not be null!");
        return this.list(session, key, clazz, newListSize0);
    }

    @SuppressWarnings("unchecked")
    final public <T> List<T> list(final HttpSession session, final SessionKey key, final Class<T> clazz, final boolean newListSize0) {
        Object obj = session.getAttribute(key.name());
        if (obj == null) {
            return newListSize0 ? new ArrayList<T>(0) : null;
        }
        return (List<T>) obj;
    }

    /** remove */
    final public void remove(final HttpServletRequest request, final SessionKey key) {
        final HttpSession session = request.getSession(false);
        Assert.notNull(session, "session can not be null!");
        this.remove(session, key);
    }

    final public void remove(final HttpSession session, final SessionKey key) {
        session.removeAttribute(key.name());
    }
}
