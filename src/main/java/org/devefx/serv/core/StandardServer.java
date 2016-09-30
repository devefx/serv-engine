package org.devefx.serv.core;

import java.beans.PropertyChangeSupport;

import org.devefx.serv.Lifecycle;
import org.devefx.serv.LifecycleException;
import org.devefx.serv.LifecycleListener;
import org.devefx.serv.Server;
import org.devefx.serv.Service;
import org.devefx.serv.util.LifecycleSupport;
import org.devefx.serv.util.StringManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardServer implements Lifecycle, Server {
	private static Logger log = LoggerFactory.getLogger(StandardServer.class);
	private static final String info = "StandardServer/1.0";
	private LifecycleSupport lifecycle = new LifecycleSupport(this);
	private int port = 11894;
	private Service[] services = new Service[0];
    private static final StringManager sm = StringManager.getManager("org.devefx.serv.core");
    private boolean started = false;
    private boolean initialized = false;
    protected PropertyChangeSupport support = new PropertyChangeSupport(this);
    protected String type;
    
    @Override
    public String getInfo() {
    	return info;
    }
    
    @Override
    public int getPort() {
    	return port;
    }
    
    @Override
    public void setPort(int port) {
    	this.port = port;
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
    public void start() throws LifecycleException {
    	if (started) {
			log.debug(sm.getString("standardBase.start.started"));
		} else {
			// TODO Auto-generated method stub
			
		}
    }
    
    @Override
    public void stop() throws LifecycleException {
    	if(started) {
    		lifecycle.fireLifecycleEvent(BEFORE_STOP_EVENT, null);
            lifecycle.fireLifecycleEvent(STOP_EVENT, null);
            started = false;
            // TODO Auto-generated method stub
            
    	}
    }
    
    public void init() throws Exception {
    	initialize();
    }
    
    @Override
    public void initialize() throws LifecycleException {
    	if (initialized) {
    		log.info(sm.getString("standardBase.initialize.initialized"));
		} else {
			lifecycle.fireLifecycleEvent(INIT_EVENT, null);
            initialized = true;
            // TODO Auto-generated method stub
            
		}
    }
    
	@Override
	public void await() {
		// TODO Auto-generated method stub
		
	}
    
    public String toString() {
        StringBuffer sb = new StringBuffer("StandardServer[");
        sb.append(this.getPort());
        sb.append("]");
        return sb.toString();
    }

	@Override
	public void addService(Service service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Service findService(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Service[] findServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeService(Service service) {
		// TODO Auto-generated method stub
		
	}
}
