package com.spring.demo.demo.metric;


import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MetricService {

   private Map<String,AtomicLong> methodsStats = new ConcurrentHashMap<>(1000);

   public void addMethodExecution(String methodName ,long time) {
      AtomicLong methodTime = methodsStats.get(methodName);
      if (methodTime != null) {
         methodTime.getAndAdd(time);
         return;
      }
      AtomicLong newMethodTime = methodsStats.putIfAbsent(methodName ,new AtomicLong(time));
      if (newMethodTime != null) {
         methodsStats.get(methodName).getAndAdd(time);
      }
   }

   public Long getMethodStats(String methodName) {
      AtomicLong methodTime = methodsStats.get(methodName);
      return methodTime != null ? methodTime.get() : 0;
   }


}
