package org.devefx.serv.core;

import org.devefx.serv.config.HandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@SuppressWarnings("rawtypes")
public class MessageDispatcher extends Thread {

	private static final Logger log = LoggerFactory.getLogger(MessageDispatcher.class);
	
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
						handler.onMessage(msg.getSender(), msg.getContent());
					} else {
						if (log.isInfoEnabled()) {
							log.info("[id: " + msg.getId() + "] UNREGISTERED");
						}
					}
				}
			}
		}
	}

}
