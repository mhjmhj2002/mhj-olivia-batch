package com.mhj.olivia.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CategoriaOlivia {

	public CategoriaOlivia(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

}
