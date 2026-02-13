package com.algaworks.algashop.billingscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class BillingSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingSchedulerApplication.class, args);
	}

}
