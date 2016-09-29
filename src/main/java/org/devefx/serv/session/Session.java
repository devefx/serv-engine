package org.devefx.serv.session;

import java.security.Principal;
import java.util.Enumeration;
import java.util.Iterator;

public interface Session {

    String getAuthType();

    void setAuthType(String authType);

    long getCreationTime();

    void setCreationTime(long time);

    String getId();

    String getIdInternal();

    void setId(String id);

    String getInfo();

    long getLastActivityTime();

    long getLastActivityTimeInternal();

    Manager getManager();

    void setManager(Manager manager);

    int getMaxInactiveInterval();

    void setMaxInactiveInterval(int interval);

    boolean isNew();

    void setNew(boolean isNew);

    Principal getPrincipal();

    void setPrincipal(Principal principal);

    void setValid(boolean valid);

    boolean isValid();

    void activate();

    void endActivate();

    void addSessionListener(SessionListener listener);

    void removeSessionListener(SessionListener listener);

    void expire();

    void recycle();

    Object getNote(String name);

    Iterator<String> getNoteNames();

    void removeNote(String name);

    void setNote(String name, Object value);

    void invalidate();

    <T> T getAttribute(String name);

    Enumeration<String> getAttributeNames();

    void setAttribute(String name, Object value);

    void removeAttribute(String name);

}
