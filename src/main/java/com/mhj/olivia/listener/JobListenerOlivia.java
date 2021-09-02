package com.mhj.olivia.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobListenerOlivia extends JobExecutionListenerSupport {
	
	private static final int MAX_QTDE_LINHAS_INCONSISTENTE = 100;
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
//	arquivoerroutil
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		super.beforeJob(jobExecution);
		log.info("Inicio execucao job.");
	}
	
	@SneakyThrows
	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		super.afterJob(jobExecution);
		taskExecutor.shutdown();
	}

}
