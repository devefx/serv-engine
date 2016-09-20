package org.devefx.serv.config.spring.schema;

import org.devefx.serv.net.tcp.TcpServer;
import org.devefx.serv.net.udp.UdpServer;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class ServNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("tcp", new ServBeanDefinitionParser(TcpServer.class, true));
		registerBeanDefinitionParser("udp", new ServBeanDefinitionParser(UdpServer.class, true));
	}
}
