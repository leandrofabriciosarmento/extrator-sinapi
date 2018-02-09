package br.com.leandrofabriciosarmento.extrator.sinapi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Composicao implements Serializable {

	private static final long serialVersionUID = 1L;

	private String idGenerico;
	private String template;
	private String banco;
	private int ano;
	private String mes;
	private String localidade;
	private String desoneracao;
	private String tipoFicha;

	private String nomeClasse;
	private String siglaClasse;
	private String nomeTipo;
	private String siglaTipo;
	private String nomeAgrupador;
	private String codigoAgrupador;
	private String nomeComposicao;
	private String codigoComposicao;
	private String unidadeMedida;
	private String origemPreco;
	private String custoTotal;
	private String custoMaoDeObra;
	private String percentualMaoDeObra;
	private String custoMaterial;
	private String percentualMaterial;
	private String custoEquipamento;
	private String percentualEquipamento;
	private String custoServicoTerceiro;
	private String percentualServicoTerceiro;
	private String custoOutros;
	private String percentualOutros;
	private String vinculo;
	private List<SubComposicao> subComposicaos = new ArrayList<>();

	public String getIdGenerico() {
		return idGenerico;
	}

	public void setIdGenerico(String idGenerico) {
		this.idGenerico = idGenerico;
	}

	public String getTipoFicha() {
		return tipoFicha;
	}

	public void setTipoFicha(String tipoFicha) {
		this.tipoFicha = tipoFicha;
	}

	public String getNomeClasse() {
		return nomeClasse;
	}

	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}

	public String getSiglaClasse() {
		return siglaClasse;
	}

	public void setSiglaClasse(String siglaClasse) {
		this.siglaClasse = siglaClasse;
	}

	public String getNomeTipo() {
		return nomeTipo;
	}

	public void setNomeTipo(String nomeTipo) {
		this.nomeTipo = nomeTipo;
	}

	public String getSiglaTipo() {
		return siglaTipo;
	}

	public void setSiglaTipo(String siglaTipo) {
		this.siglaTipo = siglaTipo;
	}

	public String getNomeAgrupador() {
		return nomeAgrupador;
	}

	public void setNomeAgrupador(String nomeAgrupador) {
		this.nomeAgrupador = nomeAgrupador;
	}

	public String getCodigoAgrupador() {
		return codigoAgrupador;
	}

	public void setCodigoAgrupador(String codigoAgrupador) {
		this.codigoAgrupador = codigoAgrupador;
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

	public String getOrigemPreco() {
		return origemPreco;
	}

	public void setOrigemPreco(String origemPreco) {
		this.origemPreco = origemPreco;
	}

	public String getCustoTotal() {
		return custoTotal;
	}

	public void setCustoTotal(String custoTotal) {
		this.custoTotal = custoTotal;
	}

	public String getCustoMaoDeObra() {
		return custoMaoDeObra;
	}

	public void setCustoMaoDeObra(String custoMaoDeObra) {
		this.custoMaoDeObra = custoMaoDeObra;
	}

	public String getPercentualMaoDeObra() {
		return percentualMaoDeObra;
	}

	public void setPercentualMaoDeObra(String percentualMaoDeObra) {
		this.percentualMaoDeObra = percentualMaoDeObra;
	}

	public String getCustoMaterial() {
		return custoMaterial;
	}

	public void setCustoMaterial(String custoMaterial) {
		this.custoMaterial = custoMaterial;
	}

	public String getPercentualMaterial() {
		return percentualMaterial;
	}

	public void setPercentualMaterial(String percentualMaterial) {
		this.percentualMaterial = percentualMaterial;
	}

	public String getCustoEquipamento() {
		return custoEquipamento;
	}

	public void setCustoEquipamento(String custoEquipamento) {
		this.custoEquipamento = custoEquipamento;
	}

	public String getPercentualEquipamento() {
		return percentualEquipamento;
	}

	public void setPercentualEquipamento(String percentualEquipamento) {
		this.percentualEquipamento = percentualEquipamento;
	}

	public String getCustoServicoTerceiro() {
		return custoServicoTerceiro;
	}

	public void setCustoServicoTerceiro(String custoServicoTerceiro) {
		this.custoServicoTerceiro = custoServicoTerceiro;
	}

	public String getPercentualServicoTerceiro() {
		return percentualServicoTerceiro;
	}

	public void setPercentualServicoTerceiro(String percentualServicoTerceiro) {
		this.percentualServicoTerceiro = percentualServicoTerceiro;
	}

	public String getCustoOutros() {
		return custoOutros;
	}

	public void setCustoOutros(String custoOutros) {
		this.custoOutros = custoOutros;
	}

	public String getPercentualOutros() {
		return percentualOutros;
	}

	public void setPercentualOutros(String percentualOutros) {
		this.percentualOutros = percentualOutros;
	}

	public String getVinculo() {
		return vinculo;
	}

	public void setVinculo(String vinculo) {
		this.vinculo = vinculo;
	}

	public List<SubComposicao> getSubComposicaos() {
		return subComposicaos;
	}

	public void setSubComposicaos(List<SubComposicao> subComposicaos) {
		this.subComposicaos = subComposicaos;
	}

	public void addSubComposicaos(SubComposicao subComposicao) {
		this.subComposicaos.add(subComposicao);
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

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
}
