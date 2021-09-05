package com.mhj.olivia.mapper;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.mhj.olivia.dto.OliviaDataDto;
import com.mhj.olivia.util.ArquivoErroUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OliviaFileLineMapper implements LineMapper<OliviaDataDto> {
	
	@Autowired
	ArquivoErroUtil arquivoErroUtil;

	@Override
	public OliviaDataDto mapLine(String line, int lineNumber) throws Exception {
		try {
			line = line.replaceAll("&amp;", "&");
			String[] split = line.split(";");
			if (split.length != 8) {
				throw new Exception("linha " + lineNumber + " inconsistente, split number: " + split.length);
			}
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
		} catch (Exception e) {
			log.error("Erro reader linha: {}. mensagem: {}", lineNumber, e.getMessage());
			arquivoErroUtil.craLinhaArquivoErro(line, ArquivoErroUtil.ARQUIVO_ERRO_READER);
		}
		return null;
	}

}
