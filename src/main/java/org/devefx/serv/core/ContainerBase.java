package org.devefx.serv.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

import org.devefx.serv.Container;
import org.devefx.serv.ContainerListener;
import org.devefx.serv.Lifecycle;
import org.devefx.serv.LifecycleException;
import org.devefx.serv.LifecycleListener;
import org.devefx.serv.Manager;
import org.devefx.serv.session.StandardManager;
import org.devefx.serv.util.LifecycleSupport;
import org.devefx.serv.util.StringManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ContainerBase implements Container, Lifecycle, Serializable {
	private static final long serialVersionUID = 480726983905097061L;
	private static Logger log = LoggerFactory.getLogger(ContainerBase.class);
	private static final String info = "ContainerBase/1.0";
	protected int backgroundProcessorDelay = -1;
	protected LifecycleSupport lifecycle = new LifecycleSupport(this);
	protected ArrayList<ContainerListener> listeners = new ArrayList<>();
	protected String logName = null;
	protected Manager manager = null;
	protected String name = null;
	protected static StringManager sm = StringManager.getManager("org.devefx.serv.core");
	protected boolean started = false;
	protected boolean initialized = false;
	protected PropertyChangeSupport support = new PropertyChangeSupport(this);
	private Thread thread = null;
    private boolean threadDone = false;
	
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
		if(started) {
			if(log.isInfoEnabled()) {
                log.info(sm.getString("containerBase.alreadyStarted", logName()));
            }
		} else {
			lifecycle.fireLifecycleEvent(BEFORE_START_EVENT, null);
			started = true;
			
			if (manager != null && manager instanceof Lifecycle) {
				((Lifecycle) manager).start();
			}
			lifecycle.fireLifecycleEvent(START_EVENT, null);
			threadStart();
			lifecycle.fireLifecycleEvent(AFTER_START_EVENT, null);
		}
	}

	@Override
	public void stop() throws LifecycleException {
		if (!started) {
			if(log.isInfoEnabled()) {
                log.info(sm.getString("containerBase.notStarted", logName()));
            }
		} else {
			lifecycle.fireLifecycleEvent(BEFORE_STOP_EVENT, null);
			threadStop();
			lifecycle.fireLifecycleEvent(STOP_EVENT, null);
			started = false;
			
			if (manager != null && manager instanceof Lifecycle) {
				((Lifecycle)manager).stop();
			}
			
			lifecycle.fireLifecycleEvent(AFTER_STOP_EVENT, null);
		}
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public Manager getManager() {
		return manager;
	}

	@Override
	public void setManager(Manager manager) {
		Manager oldManager = this.manager;
		if(oldManager != manager) {
			this.manager = manager;
			if(this.started && oldManager != null && oldManager instanceof Lifecycle) {
				try {
                    ((Lifecycle) oldManager).stop();
                } catch (LifecycleException e) {
                    log.error("ContainerBase.setManager: stop: ", e);
                }
			}
			
			if(manager != null) {
                manager.setContainer(this);
            }
			
			if(started && manager != null && manager instanceof Lifecycle) {
				try {
                    ((Lifecycle) manager).start();
                } catch (LifecycleException e) {
                    log.error("ContainerBase.setManager: start: ", e);
                }
			}
			
			support.firePropertyChange("manager", oldManager, manager);
		}

	}
	
	public void init() throws Exception {
		initialized = true;
	}
	
	public void destroy() throws Exception {
		if (started) {
			stop();
		}
		initialized = false;
	}

	@Override
	public int getBackgroundProcessorDelay() {
		return backgroundProcessorDelay;
	}

	@Override
	public void setBackgroundProcessorDelay(int delay) {
		backgroundProcessorDelay = delay;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		String oldName = this.name;
        this.name = name;
        support.firePropertyChange("name", oldName, name);
	}
	
	@Override
	public void backgroundProcess() {
		if (started) {
			if (manager != null) {
				try {
					manager.backgroundProcess();
				} catch (Exception e) {
					log.warn(sm.getString("containerBase.backgroundProcess.manager", manager), e);
				}
			}
			lifecycle.fireLifecycleEvent("periodic", null);
		}
	}
	
	@Override
	public void addContainerListener(ContainerListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	
	protected String logName() {
		if (logName != null) {
			return logName;
		}
		String loggerName = getName();
		if (loggerName == null || loggerName.equals("")) {
			loggerName = "/";
		}
		logName = "ContainerBase.[" + loggerName + "]";
        return logName;
	}
	
	protected void threadStart() {
		if (thread == null && backgroundProcessorDelay > 0) {
			threadDone = false;
			String threadName = "ContainerBackgroundProcessor[" + toString() + "]";
			thread = new Thread(new ContainerBackgroundProcessor(), threadName);
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	protected void threadStop() {
		if (thread != null) {
			threadDone = true;
			thread.interrupt();
			try {
				thread.join();
			} catch (InterruptedException e) {
			}
			thread = null;
		}
	}

	protected class ContainerBackgroundProcessor implements Runnable {
		@Override
		public void run() {
			while (!threadDone) {
				try {
					Thread.sleep(backgroundProcessorDelay * 1000L);
				} catch (InterruptedException e) {
				}
				if (!threadDone) {
					backgroundProcess();
				}
			}
		}
	}
	
}
