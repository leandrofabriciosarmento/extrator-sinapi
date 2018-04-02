package br.com.sarmentosistemas.extrator.sinapi;

import java.io.IOException;

import com.google.gson.JsonIOException;

public class Main {

	public static void main(String[] args) throws JsonIOException, IOException, InterruptedException {

		boolean renameFiles = true;
		boolean armazenarJson = false;
		boolean enviarElastiSearch = true;

		Extrator extrator = new br.com.sarmentosistemas.extrator.sinapi.Extrator(2, 2018, renameFiles, armazenarJson,
				enviarElastiSearch);
		extrator.executar();
		
//		ExecutorService service = Executors.newFixedThreadPool(2);
//		service.submit(new br.com.sarmentosistemas.extrator.sinapi.Extrator(2, 2018, renameFiles, armazenarJson,
//				enviarElastiSearch));
//
//		service.shutdown();
//		service.awaitTermination(1, TimeUnit.DAYS);
//
//		System.exit(0);
	}
}
