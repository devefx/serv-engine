package org.devefx.serv.core;

import org.devefx.serv.Engine;
import org.devefx.serv.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardEngine extends ContainerBase implements Engine {
	private static final long serialVersionUID = -2415137307499246327L;
	private static Logger log = LoggerFactory.getLogger(StandardEngine.class);
	private static final String info = "StandardEngine/1.0";
	private String defaultHost = "0.0.0.0";
	
	private Service service;
	private boolean initialized;
	
	public StandardEngine() {
		backgroundProcessorDelay = 10;
	}
	
	@Override
	public String getDefaultHost() {
		return defaultHost;
	}

	@Override
	public void setDefaultHost(String host) {
		String oldDefaultHost = this.defaultHost;
		this.defaultHost = host;
		support.firePropertyChange("defaultHost", oldDefaultHost, host);
	}

	@Override
	public Service getService() {
		return service;
	}

	@Override
	public void setService(Service service) {
		this.service = service;
	}
	
	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void init() throws Exception {
		if (!initialized) {
			initialized = true;
			
		}
	}
	
	@Override
	public void destroy() throws Exception {
		super.destroy();
		
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("StandardEngine[");
        sb.append(this.getName());
        sb.append("]");
        return sb.toString();
	}
}
