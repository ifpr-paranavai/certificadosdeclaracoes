package br.com.cedi.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Pessoa;
import br.com.cedi.uil.jpa.Transactional;



public class PessoaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaDAO pessoaDAO;
	
	@Transactional
	public void salvar(Pessoa pessoa) {	
		if(pessoa.getId() == null){
			this.pessoaDAO.inserir(pessoa);
		}else{
			this.pessoaDAO.atualizar(pessoa);
		}
	}	

	@Transactional
	public void atualizaSenha(String senha, String email) {
		this.pessoaDAO.updateSenha(senha, email);
	}
	
//	@Override
//	public void updateSenha(String senha, String email) {
//
//		String sql = "";
//		try {
//
//			sql = ("update Pessoa set senha = '" + senha + "' where email like '" + email + "'");
//			int update = manager.createQuery(sql).executeUpdate();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
}
