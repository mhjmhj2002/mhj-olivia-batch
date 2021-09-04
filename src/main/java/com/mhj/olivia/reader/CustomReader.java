package com.mhj.olivia.reader;

import org.springframework.batch.item.ResourceAware;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.Resource;

import com.mhj.olivia.dto.OliviaDataDto;

public class CustomReader extends FlatFileItemReader<OliviaDataDto> implements ResourceAware {

	private Resource resource;

	private boolean arquivoFinalizado = false;

	@Override
	public void setResource(Resource resource) {
		this.resource = resource;
		super.setResource(resource);
	}

	@Override
	protected void doClose() throws Exception {
		super.doClose();
		if (null != resource && !arquivoFinalizado) {
			arquivoFinalizado = true;
//			finalizarFileService.finalizar(resource.getFile());
		}
	}

}
