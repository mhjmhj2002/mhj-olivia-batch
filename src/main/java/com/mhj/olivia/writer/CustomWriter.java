package com.mhj.olivia.writer;

import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mhj.olivia.entity.OliviaData;
import com.mhj.olivia.service.OliviaClient;

import lombok.extern.slf4j.Slf4j;

@StepScope
@Component
@Slf4j
public class CustomWriter implements ItemWriter<OliviaData> {

	@Autowired
	private OliviaClient oliviaClient;

	@Override
	public void write(List<? extends OliviaData> items) throws Exception {
		try {
			for (OliviaData dto : items) {
				try {
					this.oliviaClient.sendOlivia(dto);
				} catch (Exception e) {
					log.error("erro envio client");
				}
			}
		} catch (Exception e) {
			log.error("erro write: ", e);
		}

	}

}
