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
@Table(name="dados")
public class Historico implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique=true)
	private Long id;
	
	@Column(length=1000)
	private String informacoes;
	
	private Boolean status = true;
		
	private String lote;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataTarefa = new Date();
	
	@ManyToOne
	@JoinColumn(name="idPessoa")
	private Pessoa pessoaLogada;	
	
	@ManyToOne
	@JoinColumn(name="idPessoaCertificada")
	private Pessoa pessoaCertificada;
	
	


	public Historico() {
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




	public String getLote() {
		return lote;
	}




	public void setLote(String lote) {
		this.lote = lote;
	}




	public Pessoa getPessoaLogada() {
		return pessoaLogada;
	}




	public void setPessoaLogada(Pessoa pessoaLogada) {
		this.pessoaLogada = pessoaLogada;
	}




	public Pessoa getPessoaCertificada() {
		return pessoaCertificada;
	}




	public void setPessoaCertificada(Pessoa pessoaCertificada) {
		this.pessoaCertificada = pessoaCertificada;
	}
		
	
	
}
