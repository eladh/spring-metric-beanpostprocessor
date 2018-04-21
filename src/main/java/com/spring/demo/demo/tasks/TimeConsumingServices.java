package com.spring.demo.demo.tasks;


import com.spring.demo.demo.metric.Metric;
import org.springframework.stereotype.Service;

@Service
public class TimeConsumingServices {

   @Metric
   public void task1() {
      randomDelay(1, 2);
   }

   @Metric
   public void task2() {
      randomDelay(1, 5);
   }

   @Metric
   public void task3() {
      randomDelay(1, 10);
   }

   private void randomDelay(float min, float max){
      int random = (int)(max * Math.random() + min);
      try {
         Thread.sleep(random * 100);
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
