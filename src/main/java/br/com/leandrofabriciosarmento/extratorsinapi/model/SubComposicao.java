package br.com.leandrofabriciosarmento.extratorsinapi.model;

import java.io.Serializable;

public class SubComposicao implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tipo;
	private String nomeComposicao;
	private String codigoComposicao;
	private String unidadeMedida;
	private String coeficiente;
	private String precoUnitario;
	private String custoTotal;

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

}
