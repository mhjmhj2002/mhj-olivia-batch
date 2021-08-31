package com.mhj.olivia.entity;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class OliviaData {
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String instituicaoFinanceira;
	private String tipoConta;
	private String nomeConta;
	private String data;
	private String descricaoTransacao;
	private String valor;
	private String categoriaOlivia;
	private String pendente;
	
	public Date getDataDate() throws ParseException {
		if (Objects.isNull(data)) {
			return null;
		}
		return dateFormat.parse(data);
	}
	public BigDecimal getValorDecimal() {
		if (Objects.isNull(valor)) {
			return null;
		}
		String[] split = valor.split(" ");
		String valor = split[1];
		valor = valor.replaceAll("\\.", "");
		valor = valor.replaceAll(",", ".");
		if (split[0].startsWith("-")) {
			return new BigDecimal(valor).multiply(new BigDecimal(-1));
		} else {
			return new BigDecimal(valor);
		}
	}

}
