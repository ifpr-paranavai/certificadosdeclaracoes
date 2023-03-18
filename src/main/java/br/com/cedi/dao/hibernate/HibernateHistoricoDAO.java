package br.com.cedi.dao.hibernate;

import java.io.Serializable;

import br.com.cedi.dao.HistoricoDAO;
import br.com.cedi.dao.LoteCertificadoDAO;
import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Historico;
import br.com.cedi.model.LoteCertificados;
import br.com.cedi.model.Pessoa;


public class HibernateHistoricoDAO extends HibernateGenericDAO<Historico, Long> implements HistoricoDAO, Serializable {

	private static final long serialVersionUID = 1L;
	

}
