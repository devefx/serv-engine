package org.devefx.serv.config.spring.schema;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import org.devefx.serv.config.HandlerRegistry;
import org.devefx.serv.core.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ServBeanDefinitionParser implements BeanDefinitionParser {

	private static final Logger log = LoggerFactory.getLogger(ServBeanDefinitionParser.class);

	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	
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

		if (beanClass.isAssignableFrom(HandlerRegistry.class)) {
			HandlerRegistry registry = new HandlerRegistry();
			String scanPackage = element.getAttribute("scan-package");
			if (scanPackage != null && !scanPackage.isEmpty()) {
				scanHandler(registry, scanPackage);
			}
			NodeList nodes = element.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (Node.ELEMENT_NODE == node.getNodeType()) {
					String className = ((Element) node).getAttribute("class");
					registerHandler(registry, className);
				}
			}
			beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(registry);
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

	private static void scanHandler(HandlerRegistry registry, String packageName) {
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + 
				ClassUtils.convertClassNameToResourcePath(packageName) + "/" + DEFAULT_RESOURCE_PATTERN;
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
		try {
			Resource[] resources = resolver.getResources(packageSearchPath);
			for (Resource resource: resources) {
				if (resource.isReadable()) {
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
					AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
					registerHandler(registry, metadata.getClassName());
				}
			}
		} catch (Exception e) {
			log.error("An error occurred:", e);
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static void registerHandler(HandlerRegistry registry, String className) {
		try {
			Class<?> handlerClass = Class.forName(className);
			if (!handlerClass.isAssignableFrom(MessageHandler.class)) {
				// throw exception
			}
			MessageHandler handler = (MessageHandler) handlerClass.newInstance();
			registry.registerHandler(handler.getId(), handler);
		} catch (Exception e) {
			log.error("An error occurred:", e);
			e.printStackTrace();
		}
	}

}
