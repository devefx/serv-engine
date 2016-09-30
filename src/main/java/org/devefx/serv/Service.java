package org.devefx.serv;

public interface Service {
	
	Container getContainer();
	
	void setContainer(Container container);
	
	String getInfo();
	
	String getName();
	
	Server getServer();
	
	void setServer(Server server);
	
	void initialize() throws LifecycleException;
}
