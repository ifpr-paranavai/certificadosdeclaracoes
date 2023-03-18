package br.com.cedi.model;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Atividade
 *
 */
@Entity
@Table(name="lotecertificados")
public class LoteCertificados implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique=true)
	private Long id;

	private String descricao;
	
	
	public LoteCertificados() {
		super();
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	public Long getId() {
		return id;
	}


}
