package org.devefx.serv.core;

import org.devefx.serv.config.HandlerRegistry;
import org.devefx.serv.net.ServerConfig;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MessageDispatcher extends Thread {

	private Queue<MessageEvent> msgQueue = new ConcurrentLinkedDeque<MessageEvent>();

	private HandlerRegistry registry;

	public MessageDispatcher(HandlerRegistry registry) {
		this.registry = registry;
	}

	public void push(MessageEvent msg) {
		synchronized (msgQueue) {
			msgQueue.add(msg);
			msgQueue.notifyAll();
		}
	}
	
	public boolean isDone() {
		return msgQueue.isEmpty();
	}
	
	@Override
	public void run() {
		while (true) {
			synchronized (msgQueue) {
				if (isDone()) {
					try {
						msgQueue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				MessageEvent msg = null;
				while ((msg = msgQueue.poll()) != null) {
					MessageHandler handler = registry.getHandler(msg.getId());
					if (handler != null) {
						handler.onMessage(msg);
					}
				}
			}
		}
	}

}
