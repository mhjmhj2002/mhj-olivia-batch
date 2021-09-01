package com.mhj.olivia.processor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.mhj.olivia.dto.OliviaDataDto;
import com.mhj.olivia.entity.OliviaData;

import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class CustomProcessor implements ItemProcessor<OliviaDataDto, OliviaData>{

	@Override
	public OliviaData process(OliviaDataDto item) throws Exception {
		return null;
	}

}
