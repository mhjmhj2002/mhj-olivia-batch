package com.mhj.olivia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@ComponentScan({"com.mhj.olivia"})
@EnableRetry
@EnableDiscoveryClient
@EnableFeignClients
public class MhjOliviaBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MhjOliviaBatchApplication.class, args);
	}

}
