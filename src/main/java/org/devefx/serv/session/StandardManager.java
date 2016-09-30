package org.devefx.serv.session;

import org.devefx.serv.Lifecycle;
import org.devefx.serv.LifecycleListener;
import org.devefx.serv.Session;
import org.devefx.serv.util.LifecycleSupport;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StandardManager extends ManagerBase implements Lifecycle, PropertyChangeListener {
    protected static final String info = "StandardManager/1.0";
    protected LifecycleSupport lifecycle = new LifecycleSupport(this);
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
            throw new IllegalStateException(sm.getString("standardManager.createSession.ise"));
        }
        return super.createSession(sessionId);
    }
    
	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		lifecycle.addLifecycleListener(listener);
	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		return lifecycle.findLifecycleListeners();
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		lifecycle.removeLifecycleListener(listener);
	}

    @Override
    public void start() {
        if (!initialized) {
            init();
        }

        if (!started) {
        	lifecycle.fireLifecycleEvent(START_EVENT, null);
            started = true;
            if (log.isDebugEnabled()) {
            	log.debug("Force random number initialization starting");
			}
            
            String dumy = generateSessionId();
            if (dumy != null && log.isDebugEnabled()) {
            	log.debug("Force random number initialization completed");
			}
        }
    }

    @Override
    public void stop() {
        if(log.isDebugEnabled()) {
            log.debug("Stopping");
        }

        if(!started) {
            throw new IllegalStateException(sm.getString("standardManager.notStarted"));
        } else {
        	lifecycle.fireLifecycleEvent(STOP_EVENT, null);
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

            random = null;
            if (initialized) {
                destroy();
            }
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent event) {
    	// TODO
    }

}
