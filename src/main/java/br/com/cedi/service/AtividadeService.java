package br.com.cedi.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import br.com.cedi.dao.AtividadeDAO;
import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Atividade;
import br.com.cedi.model.Pessoa;
import br.com.cedi.uil.jpa.Transactional;



public class AtividadeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AtividadeDAO atividadeDAO;
	
	@Transactional
	public void salvar(Atividade at) {
		if(at.getId() == null){
			this.atividadeDAO.inserir(at);
		}else{
			this.atividadeDAO.atualizar(at);
		}
		
	}
	
}
