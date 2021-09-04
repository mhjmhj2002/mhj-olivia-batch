package com.mhj.olivia.enums;

public enum EnumSimNao {

	SIM("Sim", true), NAO("Não", false);

	private String descricao;
	private boolean boleano;

	EnumSimNao(String descricao, boolean boleano) {
		this.descricao = descricao;
		this.boleano = boleano;
	}

	public String getDescricao() {
		return descricao;
	}

	public boolean isBoleano() {
		return boleano;
	}

}
