package com.mhj.olivia.dto;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
public class OliviaDataDto {

	@CsvBindByPosition(position = 0)
	private String instituicaoFinanceira;
	
	@CsvBindByPosition(position = 1)
	private String tipoConta;
	
	@CsvBindByPosition(position = 2)
	private String nomeConta;
	
	@CsvBindByPosition(position = 3)
	private String data;
	
	@CsvBindByPosition(position = 4)
	private String descricaoTransacao;
	
	@CsvBindByPosition(position = 5)
	private String valor;
	
	@CsvBindByPosition(position = 6)
	private String categoriaOlivia;
	
	@CsvBindByPosition(position = 7)
	private String pendente;

}
