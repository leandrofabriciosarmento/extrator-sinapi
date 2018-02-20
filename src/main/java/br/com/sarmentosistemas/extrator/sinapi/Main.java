package br.com.sarmentosistemas.extrator.sinapi;

import java.io.IOException;

import com.google.gson.JsonIOException;

public class Main {

	public static void main(String[] args) throws JsonIOException, IOException {
		boolean renameFiles = false;
		boolean armazenarJson = true;
		boolean enviarElastiSearch = true;
		Extrator.executar(12, 2017, renameFiles, armazenarJson, enviarElastiSearch);
	}
}
