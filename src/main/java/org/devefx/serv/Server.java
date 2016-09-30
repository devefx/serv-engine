package org.devefx.serv;

public interface Server {
	
	String getInfo();
	
	int getPort();
	
	void setPort(int port);
	
	void addService(Service service);
	
	void await();
	
	Service findService(String name);
	
	Service[] findServices();
	
	void removeService(Service service);
	
	void initialize() throws LifecycleException;
	
}
