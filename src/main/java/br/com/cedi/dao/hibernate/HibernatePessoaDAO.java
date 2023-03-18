package br.com.cedi.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Pessoa;
import br.com.cedi.uil.jpa.Transactional;

public class HibernatePessoaDAO extends HibernateGenericDAO<Pessoa, Long> implements PessoaDAO, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	//@Transactional
	@Override
	public void updateSenha(String senha, String email) {

		String sql = "";
		try {

			sql = ("update Pessoa set senha = '" + senha + "' where email like '" + email + "'");
			int update = manager.createQuery(sql).executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
//
	}
	

	

}
