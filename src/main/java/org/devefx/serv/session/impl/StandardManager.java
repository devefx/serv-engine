package org.devefx.serv.session.impl;

import org.devefx.serv.session.Session;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StandardManager extends ManagerBase implements PropertyChangeListener {
    protected static final String info = "StandardManager/1.0";

    protected int maxActiveSessions = -1;
    protected static String name = "StandardManager";
    protected boolean started = false;
    protected int rejectedSessions = 0;

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getMaxActiveSessions() {
        return this.maxActiveSessions;
    }

    public void setMaxActiveSessions(int max) {
        int oldMaxActiveSessions = maxActiveSessions;
        maxActiveSessions = max;
        support.firePropertyChange("maxActiveSessions", new Integer(oldMaxActiveSessions), new Integer(maxActiveSessions));
    }

    @Override
    public int getRejectedSessions() {
        return rejectedSessions;
    }

    @Override
    public void setRejectedSessions(int rejectedSessions) {
        this.rejectedSessions = rejectedSessions;
    }

    @Override
    public Session createSession(String sessionId) {
        if(maxActiveSessions >= 0 && sessions.size() >= maxActiveSessions) {
            ++rejectedSessions;
            throw new IllegalStateException("standardManager.createSession.ise");
        }
        return super.createSession(sessionId);
    }

    public void start() {
        if (!initialized) {
            init();
        }

        if (!started) {
            started = true;
        }

        // TODO
    }

    public void stop() {
        if(log.isDebugEnabled()) {
            log.debug("Stopping");
        }

        if(!started) {
            throw new IllegalStateException("standardManager.notStarted");
        } else {
            started = false;


            Session[] sessions = findSessions();
            for(int i = 0; i < sessions.length; ++i) {
                Session session = sessions[i];
                try {
                    if(session.isValid()) {
                        session.expire();
                    }
                } catch (Throwable throwable) {

                } finally {
                    session.recycle();
                }
            }


            if (initialized) {
                destroy();
            }
        }
        // TODO
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

    }

}
