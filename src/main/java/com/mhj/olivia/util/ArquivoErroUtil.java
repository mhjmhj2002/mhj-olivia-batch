package com.mhj.olivia.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArquivoErroUtil {

	@Value("${files.path.error}")
	private String errorPath;

	public static final String ARQUIVO_ERRO_CLIENT = "error_file_send_";

	public static final String ARQUIVO_ERRO_READER = "error_file_reader";

	public static final String ARQUIVO_ERRO_PROCESSOR = "error_file_processor";

	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}

//	public synchronized void craLinhaArquivoErro(OliviaData dto) {
//
//		try {
//			File file = new File(errorPath);
//
//			File fileErro = this.verificaArquivoErro(file.listFiles());
//			if (null != fileErro) {
//				this.escrever(fileErro.getPath(), dto);
//			} else {
//				File criarArquivo = new File(errorPath + ARQUIVO_ERRO_CLIENT + getDate() + ".csv");
//				if (!criarArquivo.createNewFile()) {
//					log.warn("Arquivo de erro nao criado");
//				}
//				this.escrever(criarArquivo.getPath(), "0" + getDate() + "1");
//				this.escrever(criarArquivo.getPath(), dto);
//			}
//
//		} catch (Exception e) {
//			log.error("Nao foi possivel escrever no arquivo de erro.");
//		}
//
//	}

	public synchronized void craLinhaArquivoErro(String linha, String tipoArquivo) {

		try {
			File file = new File(errorPath);

			File fileErro = this.verificaArquivoErro(file.listFiles(), tipoArquivo);
			if (null != fileErro) {
				this.escrever(fileErro.getPath(), linha);
			} else {
				File criarArquivo = new File(errorPath + tipoArquivo + getDate() + ".csv");
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

//	private void escrever(String path, OliviaData dto) throws IOException {
//		String texto = this.converteParaTexto(dto);
//		this.escrever(path, texto);
//	}

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

//	private String converteParaTexto(OliviaData dto) {
//		return "teste arquivo erro";
////		return dto.getLinha();
//	}

	private String getDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	private File verificaArquivoErro(File[] listFiles, String tipoArquivo) {
		if (null == listFiles) {
			return null;
		}
		for (File file : listFiles) {
			if (file.getName().matches(tipoArquivo + ".*?")) {
				return file;
			}
		}
		return null;
	}

}
