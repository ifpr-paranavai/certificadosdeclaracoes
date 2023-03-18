package br.com.cedi.model;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Atividade
 *
 */
@Entity
@Table(name="dadosPesquisa")
public class HistoricoBusca implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique=true)
	private Long id;
	
	@Column(length=1000)
	private String informacoes;
	
	private String nome;
	private String cpf;
	private String codigoValidacao;
	
	private Boolean status = true;
		
	private String tipo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataTarefa = new Date();
	

	public HistoricoBusca() {
		super();
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getInformacoes() {
		return informacoes;
	}




	public void setInformacoes(String informacoes) {
		this.informacoes = informacoes;
	}




	public Boolean getStatus() {
		return status;
	}




	public void setStatus(Boolean status) {
		this.status = status;
	}




	public String getNome() {
		return nome;
	}




	public void setNome(String nome) {
		this.nome = nome;
	}




	public String getCpf() {
		return cpf;
	}




	public void setCpf(String cpf) {
		this.cpf = cpf;
	}




	public String getCodigoValidacao() {
		return codigoValidacao;
	}




	public void setCodigoValidacao(String codigoValidacao) {
		this.codigoValidacao = codigoValidacao;
	}




	public String getTipo() {
		return tipo;
	}




	public void setTipo(String tipo) {
		this.tipo = tipo;
	}




	public Date getDataTarefa() {
		return dataTarefa;
	}




	public void setDataTarefa(Date dataTarefa) {
		this.dataTarefa = dataTarefa;
	}



}
