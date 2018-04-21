package com.spring.demo.demo.tasks;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Tasks {

   private final TimeConsumingServices myTimeConsumingServices;

   @Autowired
   public Tasks(TimeConsumingServices theMyTimeConsumingServices) {
      this.myTimeConsumingServices = theMyTimeConsumingServices;
   }

   @Scheduled(fixedDelay = 600)
   public void task1() {
      myTimeConsumingServices.task1();
   }

   @Scheduled(fixedDelay = 500)
   public void task2() {
      myTimeConsumingServices.task2();
   }

   @Scheduled(fixedDelay = 400)
   public void task3() {
      myTimeConsumingServices.task3();
   }



}
