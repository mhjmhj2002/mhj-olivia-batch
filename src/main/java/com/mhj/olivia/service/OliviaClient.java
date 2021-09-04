package com.mhj.olivia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.mhj.olivia.client.OnlineOliviaClient;
import com.mhj.olivia.entity.OliviaData;
import com.mhj.olivia.util.ArquivoErroUtil;

@Service
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
		this.onlineOliviaClient.incluir(dto);		
	}
	
	@Recover
	public synchronized void criarLinhaArquivoErro(OliviaData dto) {
		arquivoErroUtil.craLinhaArquivoErro(dto.getLinha(), ArquivoErroUtil.ARQUIVO_ERRO_CLIENT);
	}

}
