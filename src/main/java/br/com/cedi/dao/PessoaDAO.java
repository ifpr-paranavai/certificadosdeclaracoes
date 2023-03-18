package br.com.cedi.dao;


import br.com.cedi.model.Pessoa;



public interface PessoaDAO extends GenericDAO<Pessoa, Long> {

public void updateSenha(String senha, String email);
	
}
