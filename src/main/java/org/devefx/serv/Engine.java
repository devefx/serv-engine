package org.devefx.serv;

public interface Engine extends Container {
	
	String getDefaultHost();
	
	void setDefaultHost(String host);
	
	Service getService();
	
	void setService(Service service);
}
