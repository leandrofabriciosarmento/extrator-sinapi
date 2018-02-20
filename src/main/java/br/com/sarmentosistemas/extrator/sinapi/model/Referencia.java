package br.com.sarmentosistemas.extrator.sinapi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Referencia implements Serializable {

	private static final long serialVersionUID = 1L;

	private String uf;
	private String mes;
	private int ano;
	private String periodo;
	private String desoneracao;
	private List<Composicao> composicaos = new ArrayList<>();

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getDesoneracao() {
		return desoneracao;
	}

	public void setDesoneracao(String desoneracao) {
		this.desoneracao = desoneracao;
	}

	public List<Composicao> getComposicaos() {
		return composicaos;
	}

	public void setComposicaos(List<Composicao> composicaos) {
		this.composicaos = composicaos;
	}

	public void addComposicaos(Composicao composicao) {
		this.composicaos.add(composicao);
	}

	public String getMes() {
	    return mes;
	}

	public void setMes(String mes) {
	    this.mes = mes;
	}

	public int getAno() {
	    return ano;
	}

	public void setAno(int ano) {
	    this.ano = ano;
	}

}
