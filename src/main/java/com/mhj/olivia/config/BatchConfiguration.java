package com.mhj.olivia.config;

import java.io.IOException;

import javax.batch.api.listener.JobListener;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.mhj.olivia.listener.CustomListener;
import com.mhj.olivia.processor.CustomProcessor;
import com.mhj.olivia.reader.CustomReader;
import com.mhj.olivia.tasklet.CheckSystemTasklet;
import com.mhj.olivia.writer.CustomWriter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

	  @Autowired
	  public JobBuilderFactory jobBuilderFactory;

	  @Autowired
	  public StepBuilderFactory stepBuilderFactory;
	  
	  @Autowired
	  private CheckSystemTasklet checkSystemTasklet;
	  
	  @Autowired
	  private CustomReader customReader;
	  
	  @Autowired
	  private CustomProcessor customProcessor;
	  
	  @Autowired
	  private CustomWriter customWriter;

	  @Value("${files.path.resources}")
	  private String resourcesPath;
	  
	  @Value("${files.path.type}")
	  private int fileType;
	  
	  @Bean
	  public void mainJob(JobListener jobListener, Step step) {
		  jobBuilderFactory
		  .get("mainJob")
		  .incrementer(new RunIdIncrementer())
		  .listener(new CustomListener())
		  .flow(checkSystemStep())
		  .next(masterStep())
		  .end()
		  .build();
	  }

	  private Step checkSystemStep() {
		  return stepBuilderFactory
				  .get("checkSystemStep")
				  .tasklet(checkSystemTasklet)
				  .build();
	  }
	  
	  @Bean
	  @Qualifier("masterStep")
	  private Step masterStep() {
		  return stepBuilderFactory
				  .get("masterStep")
				  .partitioner("step1", partitioner())
				  .step(step1())
				  .build();
	  }

	private Step step() {
		return stepBuilderFactory
				.get("step1")
				.chunk(100)
				.reader(customReader)
				.p;
	}

	private Partitioner partitioner() {
		MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = null;
		try {
			resources = resolver.getResources("file:" + resourcesPath + "*." + fileType);
		} catch (IOException e) {
			log.error("Erro del eitura");
		}
		partitioner.setResources(resources);
		partitioner.partition(10);
		return partitioner;
	}
	  
	  

}
