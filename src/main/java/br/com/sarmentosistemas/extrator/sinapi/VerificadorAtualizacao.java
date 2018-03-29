package br.com.sarmentosistemas.extrator.sinapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import com.google.gson.Gson;

import br.com.sarmentosistemas.extrator.sinapi.model.Referencia;

public class VerificadorAtualizacao {

	/**
	 * Exemplo do arquivo: {ano:2018, mes:01}
	 */
	private static final String ARQUIVO_CONTROlE = "controle.json";

	public static void main(String[] args) throws Exception {

		Controle controle = lerArquivoControle();
		controle.setMes(controle.getMes()+1);
		boolean atualizacaoDisponivel = verificarAtualizacao(controle);
		
		System.out.println("Atualização disponível: "+atualizacaoDisponivel);
	}

	private static boolean verificarAtualizacao(Controle controle) throws Exception {

		if (controle == null) {
			throw new Exception("Arquivo de controle vazio!");
		}

		try {
			Referencia referencia = new Referencia();
			referencia.setDesoneracao("Desonerado");
			referencia.setUf("DF");
			referencia.setAno(controle.getAno());
			referencia.setMes((controle.getMes() < 10) ? "0" + controle.getMes() : controle.getMes() + "");
			referencia.setPeriodo(referencia.getMes() + referencia.getAno());
			String url = Extrator.montarURLArquivo(referencia);

			System.out.println(url);

			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
			URL website = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());

			if(rbc.isOpen()) {
				return true;
			}
			
			return false;

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		return false;

	}

	private static Controle lerArquivoControle() {
		BufferedReader br = null;
		FileReader fr = null;

		try {

			File arquivo = new File(ARQUIVO_CONTROlE);
			try {
				br = new BufferedReader(new FileReader(arquivo));
				fr = new FileReader(arquivo);
			} catch (java.io.FileNotFoundException e) {

				PrintWriter writer = new PrintWriter(arquivo, "UTF-8");
				writer.close();

				br = new BufferedReader(new FileReader(arquivo));
				fr = new FileReader(arquivo);
			}
			br = new BufferedReader(fr);

			String sCurrentLine;
			StringBuffer json = new StringBuffer("");

			while ((sCurrentLine = br.readLine()) != null) {
				json.append(sCurrentLine);
			}

			Gson gson = new Gson();
			Controle controle = gson.fromJson(json.toString(), Controle.class);

			return controle;

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		return null;
	}
}

class Controle {

	private int ano;
	private int mes;

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

}