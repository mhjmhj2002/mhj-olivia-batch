package com.mhj.olivia.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OliviaData {

	private String instituicaoFinanceira;
	private String tipoConta;
	private String nomeConta;
	private Date data;
	private String descricaoTransacao;
	private BigDecimal valor;
	private String categoriaOlivia;
	private Boolean pendente;

}
