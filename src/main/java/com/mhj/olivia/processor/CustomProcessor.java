package com.mhj.olivia.processor;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mhj.olivia.dto.OliviaDataDto;
import com.mhj.olivia.entity.CategoriaOlivia;
import com.mhj.olivia.entity.OliviaData;
import com.mhj.olivia.enums.EnumSimNao;
import com.mhj.olivia.util.ArquivoErroUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class CustomProcessor implements ItemProcessor<OliviaDataDto, OliviaData> {

	@Autowired
	private ArquivoErroUtil arquivoErroUtil;

	@Override
	public OliviaData process(OliviaDataDto item) {
		if (Objects.isNull(item)) {
			return null;
		}
		OliviaData dto = new OliviaData();
		try {
			dto.setCategoriaOlivia(new CategoriaOlivia(item.getCategoriaOlivia()));
			dto.setData(getDataDate(item.getData()));
			dto.setDescricaoTransacao(item.getDescricaoTransacao());
			dto.setInstituicaoFinanceira(item.getInstituicaoFinanceira());
			dto.setNomeConta(item.getNomeConta());
			dto.setPendente(getPendenteBoolean(item.getPendente()));
			dto.setTipoConta(item.getTipoConta());
			dto.setValor(getValorDecimal(item.getValor()));
			dto.setLinha(item.getLinha());
			dto.setNumeroLinha(item.getNumeroLinha());
			return dto;
		} catch (Exception e) {
			log.error("Erro processor linha: {}. mensagem: {}", item.getNumeroLinha(), e.getMessage());
			log.error("item: {}", item.toString());
			log.error("dto: {}", dto.toString());
			arquivoErroUtil.craLinhaArquivoErro(item.getLinha(), ArquivoErroUtil.ARQUIVO_ERRO_PROCESSOR);
		}
		return null;
	}

	private Date getDataDate(String data) throws ParseException {
		if (Objects.isNull(data)) {
			return null;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
		return dateFormat.parse(data);
	}

	private BigDecimal getValorDecimal(String valorStr) {
		if (Objects.isNull(valorStr)) {
			return null;
		}
		String[] split = valorStr.split(" ");
		String valor = split[1];
		valor = valor.replaceAll("\\.", "");
		valor = valor.replaceAll(",", ".");
		if (split[0].startsWith("-")) {
			return new BigDecimal(valor).multiply(new BigDecimal(-1));
		} else {
			return new BigDecimal(valor);
		}
	}

	private boolean getPendenteBoolean(String pendente) {
		if (EnumSimNao.NAO.getDescricao().equals(pendente)) {
			return EnumSimNao.NAO.isBoleano();
		} else {
			return EnumSimNao.SIM.isBoleano();
		}
	}

}
