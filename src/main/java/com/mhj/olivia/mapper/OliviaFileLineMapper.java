package com.mhj.olivia.mapper;

import org.springframework.batch.item.file.LineMapper;

import com.mhj.olivia.dto.OliviaDataDto;

public class OliviaFileLineMapper implements LineMapper<OliviaDataDto> {

	@Override
	public OliviaDataDto mapLine(String line, int lineNumber) throws Exception {
		return null;
	}

}
