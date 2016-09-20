package org.devefx.serv.config;

import org.devefx.serv.core.MessageHandler;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerRegistry implements Serializable {

    private Map<Short, MessageHandler> handlerMap = new ConcurrentHashMap<>(5);

    public HandlerRegistry() {

    }

    public HandlerRegistry(HandlerRegistry registry) {
        this.handlerMap = registry.handlerMap;
    }

    public void registerHandler(short id, MessageHandler handler) throws HandlerStoreException {
        if (containsHandler(id)) {
            throw new HandlerStoreException("The handler already exists: " + handler.getClass().getName());
        }
        handlerMap.put(id, handler);
    }

    public void removeHandler(short id) {
        handlerMap.remove(id);
    }

    public MessageHandler getHandler(short id) {
        return handlerMap.get(id);
    }

    public boolean containsHandler(short id) {
        return handlerMap.containsKey(id);
    }

    public int getHandlerCount() {
        return handlerMap.size();
    }
}
