package br.com.cedi.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import br.com.cedi.dao.HistoricoBuscaDAO;
import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.HistoricoBusca;
import br.com.cedi.model.Pessoa;
import br.com.cedi.uil.jpa.Transactional;



public class HistoricoBuscaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private HistoricoBuscaDAO historicoBuscaDAO;
	
	@Transactional
	public void salvar(HistoricoBusca his) {
		if(his.getId() == null){
			this.historicoBuscaDAO.inserir(his);
		}else{
			this.historicoBuscaDAO.atualizar(his);
		}
	}
	
}
