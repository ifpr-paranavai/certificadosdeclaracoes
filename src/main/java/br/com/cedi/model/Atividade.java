package br.com.cedi.model;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Atividade
 *
 */
@Entity
@Table(name="atividade")
public class Atividade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique=true)
	private Long id;

	private String atividade;
	private String responsavel;
	private String cargaHoraria;
	@ManyToOne
	@JoinColumn(name="idCertificado")
	private Certificado certificado;
	private Boolean status;

	public Atividade() {
		super();
	}
	
	

	public Certificado getCertificado() {
		return certificado;
	}



	public void setCertificado(Certificado certificado) {
		this.certificado = certificado;
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

	public String getAtividade() {
		return atividade;
	}

	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}



}
