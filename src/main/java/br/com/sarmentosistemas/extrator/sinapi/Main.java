package br.com.sarmentosistemas.extrator.sinapi;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonIOException;

public class Main {

	public static void main(String[] args) throws JsonIOException, IOException, InterruptedException {

		boolean renameFiles = false;
		boolean armazenarJson = true;
		boolean enviarElastiSearch = false;

		ExecutorService service = Executors.newFixedThreadPool(2);
		service.submit(new br.com.sarmentosistemas.extrator.sinapi.Extrator(1, 2018, renameFiles, armazenarJson,
				enviarElastiSearch));

		service.shutdown();
		service.awaitTermination(1, TimeUnit.DAYS);

		System.exit(0);
	}
}
