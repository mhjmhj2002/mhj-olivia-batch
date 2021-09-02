package com.mhj.olivia.config;

import java.io.IOException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.mhj.olivia.dto.OliviaDataDto;
import com.mhj.olivia.entity.OliviaData;
import com.mhj.olivia.listener.CustomListener;
import com.mhj.olivia.listener.JobListenerOlivia;
import com.mhj.olivia.listener.tasklet.MoveErrorFilesTasklet;
import com.mhj.olivia.mapper.OliviaFileLineMapper;
import com.mhj.olivia.processor.CustomProcessor;
import com.mhj.olivia.reader.CustomReader;
import com.mhj.olivia.tasklet.CheckSystemTasklet;
import com.mhj.olivia.writer.CustomWriter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

	  public static final int THREAD_POOL = 200;

	  @Autowired
	  public JobBuilderFactory jobBuilderFactory;

	  @Autowired
	  public StepBuilderFactory stepBuilderFactory;
	  
	  @Autowired
	  private CheckSystemTasklet checkSystemTasklet;
	  
	  @Autowired
	  private FlatFileItemReader<OliviaDataDto> customReader;
	  
	  @Value("${files.path.resources}")
	  private String resourcesPath;
	  
	  @Value("${files.path.error}")
	  private String errorPath;
	  
	  @Value("${files.path.type}")
	  private String fileType;
	  
	  @Bean
	  public Job mainJob(JobListenerOlivia jobListener, Step step) {
		  return jobBuilderFactory
		  .get("mainJob")
		  .incrementer(new RunIdIncrementer())
		  .listener(jobListener)
		  .flow(checkSystemStep())
		  .next(moveErrorFiles())
		  .next(masterStep())
		  .end()
		  .build();
	  }

	  private Step moveErrorFiles() {
		  return this.stepBuilderFactory
				  .get("moveErrorFiles")
				  .tasklet(new MoveErrorFilesTasklet(resourcesPath, errorPath))
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
	  public Step masterStep() {
		  return stepBuilderFactory
				  .get("masterStep")
				  .partitioner("step1", partitioner())
				  .step(step1())
				  .build();
	  }

	private Step step1() {
		return stepBuilderFactory
				.get("step1")
				.<OliviaDataDto, OliviaData>chunk(100)
				.reader(customReader)
				.processor(getCustomProcessor())
				.writer(getCustomWriter())
				.listener(getCustomListener())
				.taskExecutor(taskExecutorReader())
				.throttleLimit(THREAD_POOL)
				.build();
	}
	
	@Bean
	@StepScope
	@Qualifier("oliviaItemReader")
	@DependsOn("oliviaPartitioner")
	public FlatFileItemReader<OliviaDataDto> oliviaItemReader(@Value("#{stepExecutionContext['fileName']}") String fileName) throws Exception{
		CustomReader reader = new CustomReader();
		reader.setName("oliviaItemReader");
		reader.setStrict(false);
		reader.setLinesToSkip(0);
		reader.setLineMapper(new OliviaFileLineMapper());
		try {
			reader.setResource(new UrlResource(fileName));
			reader.afterPropertiesSet();
		} catch (Exception e) {
			log.error("Erro reader: ", e);
			throw e;
		}
		return reader;
	}

	@Bean(name = "taskExecutorReader")
	public TaskExecutor taskExecutorReader() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(THREAD_POOL);
		executor.setThreadNamePrefix("thread-batch");
		executor.initialize();
		return executor;
	}
	
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setCorePoolSize(10);
		taskExecutor.setQueueCapacity(10);
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
	}

	public CustomListener getCustomListener() {
		return new CustomListener();
	}

	@Bean
	public CustomWriter getCustomWriter() {
		return new CustomWriter();
	}

	@Bean
	public CustomProcessor getCustomProcessor() {
		return new CustomProcessor();
	}

	@Bean("oliviaPartitioner")
	@StepScope
	public Partitioner partitioner() {
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
