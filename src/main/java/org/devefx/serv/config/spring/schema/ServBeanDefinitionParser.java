package org.devefx.serv.config.spring.schema;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ServBeanDefinitionParser implements BeanDefinitionParser {

	private final Class<?> beanClass;
	
	private final boolean required;
	
	public ServBeanDefinitionParser(Class<?> beanClass, boolean required) {
        this.beanClass = beanClass;
        this.required = required;
    }
	
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		
		String id = element.getAttribute("id");
		if ((id == null || id.isEmpty()) && required) {
			String generatedBeanName = element.getAttribute("name");
			if (generatedBeanName == null || generatedBeanName.length() == 0) {
				generatedBeanName = beanClass.getName();
			}
			id = generatedBeanName; 
			int counter = 2;
			while(parserContext.getRegistry().containsBeanDefinition(id)) {
                id = generatedBeanName + (counter ++);
            }
		}
		if (id != null && !id.isEmpty()) {
			if (parserContext.getRegistry().containsBeanDefinition(id)) {
				throw new IllegalStateException("Duplicate spring bean id " + id);
			}
			parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
		}
		
		for (Method setter : beanClass.getMethods()) {
			String name = setter.getName();
			if (name.length() > 3 && name.startsWith("set")
					&& Modifier.isPublic(setter.getModifiers())
					&& setter.getParameterTypes().length == 1) {
				Class<?> type = setter.getParameterTypes()[0];
				String property = name.substring(3).toLowerCase();
				String value = element.getAttribute(property);
				if (value != null) {
					value = value.trim();
					if (!value.isEmpty()) {
						Object reference;
						if (isPrimitive(type)) {
							reference = value;
						} else {
							if (parserContext.getRegistry().containsBeanDefinition(value)) {
								BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(value);
								if (!refBean.isSingleton()) {
									throw new IllegalStateException("The exported service ref " + value + " must be singleton! Please set the " + value + " bean scope to singleton, eg: <bean id=\"" + value+ "\" scope=\"singleton\" ...>");
								}
							}
							reference = new RuntimeBeanReference(value);
						}
						beanDefinition.getPropertyValues().addPropertyValue(property, reference);
					}
				}
			}
		}
		return beanDefinition;
	}
	
    private static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || cls == Boolean.class || cls == Byte.class
                || cls == Character.class || cls == Short.class || cls == Integer.class
                || cls == Long.class || cls == Float.class || cls == Double.class
                || cls == String.class || cls == Date.class || cls == Class.class;
    }

}
