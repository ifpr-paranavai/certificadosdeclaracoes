package br.com.cedi.util.utilitarios;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.dao.hibernate.HibernatePessoaDAO;
import br.com.cedi.model.Pessoa;

@RequestScoped
public class RetornaLogado {
	@Inject
	PessoaDAO pessoaDAO;

	/*
	 * retorna o email do usu�rio logado, nesse caso ser� o email, pois ele �
	 * utilizado para fazer login no sistema
	 */
	public static String getLogado() {
		String logado = SecurityContextHolder.getContext().getAuthentication().getName();
		return logado;
	}

	public Pessoa getPessoaLogada() {

		Pessoa p = null;
		List<Pessoa> lp = pessoaDAO.listarCondicao("email like '%" + getLogado() + "%'");
		if (lp.size() > 0) {
			p = lp.get(0);
		}
		return p;
	}

}
