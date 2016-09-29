package org.devefx.serv.session.impl;

import org.devefx.serv.session.Manager;
import org.devefx.serv.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ManagerBase implements Manager {
    protected Logger log = LoggerFactory.getLogger(ManagerBase.class);

    private static final String info = "ManagerBase/1.0";
    protected int maxInactiveInterval = 60;
    protected int sessionIdLength = 16;
    protected static String name = "ManagerBase";

    protected int sessionMaxAliveTime;
    protected int sessionAverageAliveTime;
    protected int expiredSessions = 0;
    protected Map<String, Session> sessions = new ConcurrentHashMap<>();
    protected int sessionCounter = 0;
    protected int maxActive = 0;

    protected boolean initialized = false;
    protected long processingTime = 0L;
    private int count = 0;
    protected int processExpiresFrequency = 6;

    protected PropertyChangeSupport support = new PropertyChangeSupport(this);


    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        int oldMaxInactiveInterval = maxInactiveInterval;
        maxInactiveInterval = interval;
        support.firePropertyChange("maxInactiveInterval", new Integer(oldMaxInactiveInterval), new Integer(maxInactiveInterval));
    }

    @Override
    public int getSessionIdLength() {
        return sessionIdLength;
    }

    @Override
    public void setSessionIdLength(int length) {
        int oldSessionIdLength = sessionIdLength;
        sessionIdLength = length;
        support.firePropertyChange("sessionIdLength", new Integer(oldSessionIdLength), new Integer(sessionIdLength));
    }

    public String getName() {
        return name;
    }

    public long getProcessingTime() {
        return this.processingTime;
    }

    public void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }

    public int getProcessExpiresFrequency() {
        return this.processExpiresFrequency;
    }

    public void setProcessExpiresFrequency(int processExpiresFrequency) {
        if(processExpiresFrequency > 0) {
            int oldProcessExpiresFrequency = this.processExpiresFrequency;
            this.processExpiresFrequency = processExpiresFrequency;
            this.support.firePropertyChange("processExpiresFrequency", new Integer(oldProcessExpiresFrequency), new Integer(this.processExpiresFrequency));
        }
    }

    @Override
    public int getSessionCounter() {
        return sessionCounter;
    }

    @Override
    public void setSessionCounter(int counter) {
        this.sessionCounter = counter;
    }

    @Override
    public int getMaxActive() {
        return maxActive;
    }

    @Override
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    @Override
    public int getActiveSessions() {
        return sessions.size();
    }

    @Override
    public int getExpiredSessions() {
        return expiredSessions;
    }

    @Override
    public void setExpiredSessions(int expiredSessions) {
        this.expiredSessions = expiredSessions;
    }

    @Override
    public int getSessionMaxAliveTime() {
        return sessionMaxAliveTime;
    }

    @Override
    public void setSessionMaxAliveTime(int maxAliveTime) {
        this.sessionMaxAliveTime = maxAliveTime;
    }

    @Override
    public int getSessionAverageAliveTime() {
        return sessionAverageAliveTime;
    }

    @Override
    public void setSessionAverageAliveTime(int averageAliveTime) {
        this.sessionAverageAliveTime = averageAliveTime;
    }

    @Override
    public void add(Session session) {
        sessions.put(session.getIdInternal(), session);
        int size = sessions.size();
        if(size > maxActive) {
            maxActive = size;
        }
    }

    @Override
    public void remove(Session session) {
        sessions.remove(session.getIdInternal());
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public Session createEmptySession() {
        return new StandardSession(this);
    }

    @Override
    public Session createSession(String sessionId) {
        Session session = this.createEmptySession();
        session.setNew(true);
        session.setValid(true);
        session.setCreationTime(System.currentTimeMillis());
        session.setMaxInactiveInterval(this.maxInactiveInterval);
        if(sessionId == null) {
            sessionId = this.generateSessionId();
        }

        session.setId(sessionId);
        ++this.sessionCounter;
        return session;
    }

    @Override
    public Session findSession(String id) throws IOException {
        return id == null ? null : sessions.get(id);
    }

    @Override
    public Session[] findSessions() {
        return sessions.values().toArray(new Session[0]);
    }

    @Override
    public void backgroundProcess() {
        count = (count + 1) % processExpiresFrequency;
        if(count == 0) {
            processExpires();
        }
    }

    public void processExpires() {
        long timeNow = System.currentTimeMillis();
        Session[] sessions = findSessions();
        int expireHere = 0;
        if(log.isDebugEnabled()) {
            log.debug("Start expire sessions " + getName() + " at " + timeNow + " sessioncount " + sessions.length);
        }

        for(int timeEnd = 0; timeEnd < sessions.length; ++timeEnd) {
            if(sessions[timeEnd] != null && !sessions[timeEnd].isValid()) {
                ++expireHere;
            }
        }

        long var7 = System.currentTimeMillis();
        if(log.isDebugEnabled()) {
            log.debug("End expire sessions " + getName() + " processingTime " + (var7 - timeNow) + " expired sessions: " + expireHere);
        }

        processingTime += var7 - timeNow;
    }

    public void init() {
        if (!initialized) {
            initialized = true;

        }
        // TODO
    }

    public void destroy() {
        // TODO
    }

    protected synchronized String generateSessionId() {
        // TODO

        return null;
    }
}
