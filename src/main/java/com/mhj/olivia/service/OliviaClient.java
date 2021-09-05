package com.mhj.olivia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.mhj.olivia.client.OnlineOliviaClient;
import com.mhj.olivia.entity.OliviaData;
import com.mhj.olivia.util.ArquivoErroUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OliviaClient {
	
	@Autowired
	ArquivoErroUtil arquivoErroUtil;
	
	@Autowired
	OnlineOliviaClient onlineOliviaClient;
	
	@Retryable(
			value = { Exception.class },
			maxAttempts = 3,
			backoff = @Backoff(delay = 500))
	public void sendOlivia(OliviaData dto) {
		this.callRest(dto);
	}

	private void callRest(OliviaData dto) {
		ResponseEntity<Void> retorno = this.onlineOliviaClient.incluir(dto);
		if (HttpStatus.ALREADY_REPORTED.equals(retorno.getStatusCode())) {
			log.info("registro duplicado: {}", dto.toString());
			return;
		}
	}
	
	@Recover
	public synchronized void criarLinhaArquivoErro(OliviaData dto) {
		arquivoErroUtil.craLinhaArquivoErro(dto.getLinha(), ArquivoErroUtil.ARQUIVO_ERRO_CLIENT);
	}

}
