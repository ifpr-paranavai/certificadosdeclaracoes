package br.com.cedi.model;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

/**
 * Entity implementation class for Entity: Atividade
 *
 */
@Entity
@Table(name="matricula")
public class Matricula implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique=true)
	private Long id;

	private String numeroMatricula;
	private Boolean status = true;
	private Boolean ativo = true;
	@ManyToOne
	@JoinColumn(name="idPessoa")
	private Pessoa pessoa;
	private String formasOferta;
	private String anoIngresso;
	
	private String curso;


	public Matricula() {
		super();
	}
		
	
	public String getAnoIngresso() {
		return anoIngresso;
	}


	public void setAnoIngresso(String anoIngresso) {
		this.anoIngresso = anoIngresso;
	}


	public String getFormasOferta() {
		return formasOferta;
	}



	public void setFormasOferta(String formasOferta) {
		this.formasOferta = formasOferta;
	}



	public String getCurso() {
		return curso;
	}



	public void setCurso(String curso) {
		this.curso = curso;
	}



	public String getNumeroMatricula() {
		return numeroMatricula;
	}



	public void setNumeroMatricula(String numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}



	public Boolean getAtivo() {
		return ativo;
	}



	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}



	public Pessoa getPessoa() {
		return pessoa;
	}



	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}



	public Boolean getStatus() {
		if(status == null){
			status=true;
		}
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	
}
