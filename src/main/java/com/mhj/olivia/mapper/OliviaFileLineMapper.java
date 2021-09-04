package com.mhj.olivia.mapper;

import org.springframework.batch.item.file.LineMapper;

import com.mhj.olivia.dto.OliviaDataDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OliviaFileLineMapper implements LineMapper<OliviaDataDto> {

	@Override
	public OliviaDataDto mapLine(String line, int lineNumber) throws Exception {
		String[] split = line.split(";");
		OliviaDataDto dto = new OliviaDataDto();
		dto.setInstituicaoFinanceira(split[0]);
		dto.setTipoConta(split[1]);
		dto.setNomeConta(split[2]);
		dto.setData(split[3]);
		dto.setDescricaoTransacao(split[4]);
		dto.setValor(split[5]);
		dto.setCategoriaOlivia(split[6]);
		dto.setPendente(split[7]);
		dto.setLinha(line);
		dto.setNumeroLinha(lineNumber);
//		log.info("linha {}: {}", lineNumber, line);
		return dto;
	}

}
