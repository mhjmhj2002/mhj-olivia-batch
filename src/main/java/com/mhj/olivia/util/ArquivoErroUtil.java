package com.mhj.olivia.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mhj.olivia.entity.OliviaData;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArquivoErroUtil {

	@Value("${files.path.error}")
	private String errorPath;

	private static final String ARQUIVO_ERRO = "error_file_";

	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}

	public synchronized void craLinhaArquivoErro(OliviaData dto) {

		try {
			File file = new File(errorPath);

			File fileErro = this.verificaArquivoErro(file.listFiles());
			if (null != fileErro) {
				this.escrever(fileErro.getPath(), dto);
			} else {
				File criarArquivo = new File(errorPath + ARQUIVO_ERRO + getDate() + ".txt");
				if (!criarArquivo.createNewFile()) {
					log.warn("Arquivo de erro nao criado");
				}
				this.escrever(criarArquivo.getPath(), "0" + getDate() + "1");
				this.escrever(criarArquivo.getPath(), dto);
			}

		} catch (Exception e) {
			log.error("Nao foi possivel escrever no arquivo de erro.");
		}

	}

	public synchronized void craLinhaArquivoErro(String linha) {

		try {
			File file = new File(errorPath);

			File fileErro = this.verificaArquivoErro(file.listFiles());
			if (null != fileErro) {
				this.escrever(fileErro.getPath(), linha);
			} else {
				File criarArquivo = new File(errorPath + ARQUIVO_ERRO + getDate() + ".txt");
				if (!criarArquivo.createNewFile()) {
					log.warn("Arquivo de erro nao criado");
				}
				this.escrever(criarArquivo.getPath(), "0" + getDate() + "1");
				this.escrever(criarArquivo.getPath(), linha);
			}

		} catch (Exception e) {
			log.error("Nao foi possivel escrever no arquivo de erro.");
		}

	}

	private void escrever(String path, OliviaData dto) throws IOException {
		String texto = this.converteParaTexto(dto);
		this.escrever(path, texto);
	}

	private synchronized void escrever(String path, String texto) throws IOException {
		FileWriter fw = new FileWriter(path, true);
		BufferedWriter conexao = new BufferedWriter(fw);

		try {
			conexao.write(texto);
			conexao.newLine();
		} catch (Exception e) {
			log.error("erro ao escrever no arquivo");
		} finally {
			conexao.close();
		}

	}

	private String converteParaTexto(OliviaData dto) {
		return "teste arquivo erro";
//		return dto.getLinha();
	}

	private String getDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	private File verificaArquivoErro(File[] listFiles) {
		if (null == listFiles) {
			return null;
		}
		for (File file : listFiles) {
			if (file.getName().matches(ARQUIVO_ERRO + ".*?")) {
				return file;
			}
		}
		return null;
	}

}
