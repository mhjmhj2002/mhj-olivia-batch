package com.mhj.olivia.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.mhj.olivia.entity.OliviaData;

public class CustomWriter implements ItemWriter<OliviaData> {

	@Override
	public void write(List<? extends OliviaData> items) throws Exception {
		
	}

}
