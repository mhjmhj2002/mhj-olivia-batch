package com.mhj.olivia.processor;

import org.springframework.batch.item.ItemProcessor;

import com.mhj.olivia.dto.OliviaDataDto;
import com.mhj.olivia.entity.OliviaData;

public class CustomProcessor implements ItemProcessor<OliviaDataDto, OliviaData>{

	@Override
	public OliviaData process(OliviaDataDto item) throws Exception {
		return null;
	}

}
