package org.devefx.serv.config;

import org.devefx.serv.core.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class HandlerRegistry implements Serializable {

	private static final long serialVersionUID = 2459153909635470874L;
	
	private static final Logger log = LoggerFactory.getLogger(HandlerRegistry.class);
	
	private Map<Object, MessageHandler> handlerMap = new ConcurrentHashMap<>(5);

    public HandlerRegistry() {

    }

    public HandlerRegistry(HandlerRegistry registry) {
        this.handlerMap = registry.handlerMap;
    }

    public void registerHandler(Object id, MessageHandler handler) throws HandlerStoreException {
        if (containsHandler(id)) {
            throw new HandlerStoreException("The handler already exists: " + handler.getClass().getName());
        }
        handlerMap.put(id, handler);
		if (log.isInfoEnabled()) {
			log.info(String.format("[id: %d] registerHandler: %s", handler.getId(), handler.getClass()));
		}
    }

    public void removeHandler(Object id) {
        handlerMap.remove(id);
    }

    public MessageHandler getHandler(Object id) {
        return handlerMap.get(id);
    }

    public boolean containsHandler(Object id) {
        return handlerMap.containsKey(id);
    }

    public int getHandlerCount() {
        return handlerMap.size();
    }
}
