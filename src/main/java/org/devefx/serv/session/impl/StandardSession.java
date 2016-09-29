package org.devefx.serv.session.impl;

import org.devefx.serv.session.Manager;
import org.devefx.serv.session.Session;
import org.devefx.serv.session.SessionListener;
import org.devefx.serv.util.Enumerator;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StandardSession implements Session, Serializable {
    protected static final boolean ACTIVITY_CHECK;
    protected static final String[] EMPTY_ARRAY;
    protected static final String NOT_SERIALIZED = "___NOT_SERIALIZABLE_EXCEPTION___";
    protected Map<String, Object> attributes = new ConcurrentHashMap<>();
    protected transient String authType = null;
    protected long creationTime = 0L;
    protected static final String[] excludedAttributes;
    protected transient volatile boolean expiring = false;

    protected String id = null;
    protected static final String info = "StandardSession/1.0";
    protected volatile long lastActivityTime;
    protected transient ArrayList<SessionListener> listeners;
    protected transient Manager manager;
    protected int maxInactiveInterval;
    protected boolean isNew;
    protected volatile boolean isValid;
    protected transient Map<String, Object> notes;
    protected transient Principal principal;

    protected transient PropertyChangeSupport support;
    protected volatile long thisActivityTime;
    protected transient AtomicInteger accessCount;

    public StandardSession(Manager manager) {
        this.lastActivityTime = this.creationTime;
        this.listeners = new ArrayList<>();
        this.manager = null;
        this.maxInactiveInterval = -1;
        this.isNew = false;
        this.isValid = false;
        this.notes = new Hashtable<>();
        this.principal = null;
        this.support = new PropertyChangeSupport(this);
        this.thisActivityTime = this.creationTime;
        this.accessCount = null;
        this.manager = manager;
        if (ACTIVITY_CHECK) {
            this.accessCount = new AtomicInteger();
        }
    }

    @Override
    public String getAuthType() {
        return authType;
    }

    @Override
    public void setAuthType(String authType) {
        String oldAuthType = this.authType;
        this.authType = authType;
        this.support.firePropertyChange("authType", oldAuthType, this.authType);
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void setCreationTime(long time) {
        creationTime = time;
        lastActivityTime = time;
        thisActivityTime = time;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getIdInternal() {
        return id;
    }

    @Override
    public void setId(String id) {
        // TODO
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public long getLastActivityTime() {
        if (!isValidInternal()) {
            throw new IllegalStateException("standardSession.getLastAccessedTime.ise");
        }
        return lastActivityTime;
    }

    @Override
    public long getLastActivityTimeInternal() {
        return lastActivityTime;
    }

    @Override
    public Manager getManager() {
        return manager;
    }

    @Override
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
        if (isValid && interval == 0) {
            expire();
        }
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public void setPrincipal(Principal principal) {
        Principal oldPrincipal = this.principal;
        this.principal = principal;
        this.support.firePropertyChange("principal", oldPrincipal, this.principal);
    }

    @Override
    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    @Override
    public boolean isValid() {
        if(this.expiring) {
            return true;
        } else if(!this.isValid) {
            return false;
        } else if(ACTIVITY_CHECK && this.accessCount.get() > 0) {
            return true;
        } else {
            if(this.maxInactiveInterval >= 0) {
                long timeNow = System.currentTimeMillis();
                int timeIdle = (int)((timeNow - this.thisActivityTime) / 1000L);
                if(timeIdle >= this.maxInactiveInterval) {
                    this.expire(true);
                }
            }
            return this.isValid;
        }
    }

    @Override
    public void activate() {
        this.lastActivityTime = this.thisActivityTime;
        this.thisActivityTime = System.currentTimeMillis();
        if(ACTIVITY_CHECK) {
            accessCount.incrementAndGet();
        }
    }

    @Override
    public void endActivate() {
        isNew = false;
        if (ACTIVITY_CHECK) {
            accessCount.decrementAndGet();
        }
    }

    @Override
    public void addSessionListener(SessionListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeSessionListener(SessionListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void expire() {
        expire(true);
    }

    public void expire(boolean notify) {
        // TODO
    }

    @Override
    public void recycle() {
        this.attributes.clear();
        this.setAuthType(null);
        this.creationTime = 0L;
        this.expiring = false;
        this.id = null;
        this.lastActivityTime = 0L;
        this.maxInactiveInterval = -1;
        this.notes.clear();
        this.setPrincipal(null);
        this.isNew = false;
        this.isValid = false;
        this.manager = null;
    }

    @Override
    public Object getNote(String name) {
        return notes.get(name);
    }

    @Override
    public Iterator<String> getNoteNames() {
        return notes.keySet().iterator();
    }

    @Override
    public void removeNote(String name) {
        notes.remove(name);
    }

    @Override
    public void setNote(String name, Object value) {
        notes.put(name, value);
    }

    @Override
    public void invalidate() {
        if (!isValidInternal()) {
            throw new IllegalStateException("standardSession.invalidate.ise");
        }
        expire();
    }

    @Override
    public <T> T getAttribute(String name) {
        if (!isValidInternal()) {
            throw new IllegalStateException("standardSession.getAttributeNames.ise");
        }
        return name == null ? null : (T) attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        if (!isValidInternal()) {
            throw new IllegalStateException("standardSession.getAttributeNames.ise");
        }
        return new Enumerator<String>(attributes.keySet(), false);
    }

    @Override
    public void setAttribute(String name, Object value) {
        setAttribute(name, value, true);
    }

    public void setAttribute(String name, Object value, boolean notify) {
        // TODO
    }

    @Override
    public void removeAttribute(String name) {
        removeAttribute(name, true);
    }

    public void removeAttribute(String name, boolean notify) {
        if(!isValidInternal()) {
            throw new IllegalStateException("standardSession.removeAttribute.ise");
        } else {
            removeAttributeInternal(name, notify);
        }
    }

    protected void removeAttributeInternal(String name, boolean notify) {
        // TODO
    }

    protected boolean isValidInternal() {
        return this.isValid || this.expiring;
    }

    static {
        ACTIVITY_CHECK = Boolean.valueOf(System.getProperty("org.devefx.serv.session.impl.StandardSession.ACTIVITY_CHECK", "false")).booleanValue();
        EMPTY_ARRAY = new String[0];
        excludedAttributes = new String[]{"javax.security.auth.subject"};
    }
}
