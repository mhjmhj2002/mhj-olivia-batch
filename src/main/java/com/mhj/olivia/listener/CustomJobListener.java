package com.mhj.olivia.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Iniciando Job {}", jobExecution.getJobConfigurationName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("Finalizando Job {}", jobExecution.getJobConfigurationName());
	}

}
