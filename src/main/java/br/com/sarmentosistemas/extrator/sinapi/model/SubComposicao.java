package br.com.sarmentosistemas.extrator.sinapi.model;

import java.io.Serializable;

public class SubComposicao implements Serializable {

	private static final long serialVersionUID = 1L;

	private String idGenerico;
	private String banco;
	private int ano;
	private String mes;
	private String localidade;
	private String desoneracao;
	private String tipo;
	private String nomeComposicao;
	private String codigoComposicao;
	private String unidadeMedida;
	private String coeficiente;
	private String precoUnitario;
	private String custoTotal;

	public String getIdGenerico() {
		return idGenerico;
	}

	public void setIdGenerico(String idGenerico) {
		this.idGenerico = idGenerico;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNomeComposicao() {
		return nomeComposicao;
	}

	public void setNomeComposicao(String nomeComposicao) {
		this.nomeComposicao = nomeComposicao;
	}

	public String getCodigoComposicao() {
		return codigoComposicao;
	}

	public void setCodigoComposicao(String codigoComposicao) {
		this.codigoComposicao = codigoComposicao;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public String getCoeficiente() {
		return coeficiente;
	}

	public void setCoeficiente(String coeficiente) {
		this.coeficiente = coeficiente;
	}

	public String getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(String precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public String getCustoTotal() {
		return custoTotal;
	}

	public void setCustoTotal(String custoTotal) {
		this.custoTotal = custoTotal;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getDesoneracao() {
		return desoneracao;
	}

	public void setDesoneracao(String desoneracao) {
		this.desoneracao = desoneracao;
	}
}
