package com.spring.demo.demo.metric;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class MetricBeanPostProcessor implements BeanPostProcessor{

   private final Map<String, Class<?>> originalMetricClasses = new HashMap<>();

   private final MetricService myMetricService;

   @Autowired
   public MetricBeanPostProcessor(MetricService myMetricService) {
      this.myMetricService = myMetricService;
   }

   @Override
   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
      Class<?> originalMetricClass = bean.getClass();
      for (Method method : originalMetricClass.getMethods()) {
         if (method.isAnnotationPresent(Metric.class)) {
            originalMetricClasses.put(beanName, originalMetricClass);
            break;
         }
      }

      return bean;
   }

   @Override
   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
      Class<?> originalBeanClass = originalMetricClasses.get(beanName);
      if (originalBeanClass != null) {
         return createMetricProxy(originalBeanClass);
      }

      return bean;
   }

   private Object createMetricProxy(Class<?> theOriginalBeanClass) {
      Enhancer enhancer = new Enhancer();
      enhancer.setSuperclass(theOriginalBeanClass);
      enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
         if (method.isAnnotationPresent(Metric.class)) {
            StopWatch timer = new StopWatch();
            timer.start();
            try {
               return proxy.invokeSuper(obj, args);
            } finally {
               timer.stop();
               myMetricService.addMethodExecution(prettyMethodName(method) ,timer.getTotalTimeMillis());
            }
         } else {
            return proxy.invokeSuper(obj, args);
         }

      });

      return enhancer.create();
   }

   private String prettyMethodName(Method method) {
      return method.getDeclaringClass().getSimpleName() +
            "." +
            method.getName() +
            "(" +
            Arrays.stream(method.getParameterTypes())
                  .map(Class::getSimpleName)
                  .collect(Collectors.joining(", ")) +
            ")";

   }
}