package br.com.cedi.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.cedi.dao.CertificadoDAO;
import br.com.cedi.model.Certificado;
import br.com.cedi.uil.jpa.Transactional;



public class CertificadoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CertificadoDAO certificadoDAO;
	
	@Transactional
	public void salvar(Certificado pedido) {	
		if(pedido.getId() == null){
			this.certificadoDAO.inserir(pedido);
		}else{
			this.certificadoDAO.atualizar(pedido);
		}
		
	}
	
}
