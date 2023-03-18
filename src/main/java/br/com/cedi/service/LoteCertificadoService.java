package br.com.cedi.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import br.com.cedi.dao.LoteCertificadoDAO;
import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.LoteCertificados;
import br.com.cedi.model.Pessoa;
import br.com.cedi.uil.jpa.Transactional;



public class LoteCertificadoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private LoteCertificadoDAO loteCertificadoDAO;
	
	@Transactional
	public void salvar(LoteCertificados lc) {	
		if(lc.getId() == null){
			this.loteCertificadoDAO.inserir(lc);
		}else{
			this.loteCertificadoDAO.atualizar(lc);
		}
	}
	
}
