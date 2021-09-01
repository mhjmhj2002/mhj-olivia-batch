package com.mhj.olivia.listener.tasklet;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MoveErrorFilesTasklet implements Tasklet {
	
	private String resourcePath;
	
	private String errorPath;

	public MoveErrorFilesTasklet(String resourcePath, String errorPath) {
		super();
		this.resourcePath = resourcePath;
		this.errorPath = errorPath;
		
		this.moveFiles();
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		return RepeatStatus.FINISHED;
	}
	
	public void moveFiles() {
		log.info("Movendo arquivos de erro para processar...");
		
		final File resourceDir = new File(resourcePath);
		final File errorDir = new File(errorPath);
		
		if (!resourceDir.exists()) {
			resourceDir.mkdirs();
		}
		
		if (!errorDir.exists()) {
			errorDir.mkdirs();
		}
		
		Arrays.asList(errorDir.listFiles((dir, name) -> name.matches(".*?")))
		.stream()
		.forEach(singleFile -> {
			try {
				FileUtils.moveFileToDirectory(singleFile, resourceDir, false);
				if (!singleFile.delete()) {
					log.warn("Arquivo nao excluido");
				}
				log.info("Arquivo de erro movido com sucesso.");
			} catch (Exception e) {
				log.error("Erro ao mover arquivo de erro para resource.", e);
			}
		});
	}

}
