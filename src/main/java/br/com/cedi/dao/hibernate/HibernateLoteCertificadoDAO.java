package br.com.cedi.dao.hibernate;

import java.io.Serializable;

import br.com.cedi.dao.LoteCertificadoDAO;
import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.LoteCertificados;
import br.com.cedi.model.Pessoa;


public class HibernateLoteCertificadoDAO extends HibernateGenericDAO<LoteCertificados, Long> implements LoteCertificadoDAO, Serializable {

	private static final long serialVersionUID = 1L;
	

}
