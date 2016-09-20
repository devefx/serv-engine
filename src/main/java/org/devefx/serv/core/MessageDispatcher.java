package org.devefx.serv.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;


public class MessageDispatcher extends Thread {

	private Queue<MessageEvent> msgQueue = new ConcurrentLinkedDeque<MessageEvent>();
	
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
					System.out.println("处理消息：" + msg);
				}
			}
		}
	}

}
