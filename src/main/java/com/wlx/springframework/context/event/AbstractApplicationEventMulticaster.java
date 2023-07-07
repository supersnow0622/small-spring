package com.wlx.springframework.context.event;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.context.ApplicationEvent;
import com.wlx.springframework.context.ApplicationListener;
import com.wlx.springframework.utils.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster {

    private Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    public Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener> results = new LinkedList<>();
        for (ApplicationListener listener : applicationListeners) {
            if (supportEvent(listener, event)) {
                results.add(listener);
            }
        }
        return results;
    }

    private boolean supportEvent(ApplicationListener<ApplicationEvent> listener, ApplicationEvent event) {
        Class<? extends ApplicationListener> applicationListenerClass = listener.getClass();
        Class<?> targetClass = ClassUtils.isCglibProxyClass(applicationListenerClass) ?
                applicationListenerClass.getSuperclass() : applicationListenerClass;
        Type genericInterface = targetClass.getGenericInterfaces()[0];

        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        String typeName = actualTypeArgument.getTypeName();
        Class<?> eventClass;
        try {
            eventClass = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name: " + typeName);
        }
        return eventClass.isAssignableFrom(event.getClass());
    }
}
