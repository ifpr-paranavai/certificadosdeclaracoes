package br.com.cedi.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Pessoa;
import br.com.cedi.service.PessoaService;
import br.com.cedi.util.utilitarios.CriptografiaSenha;


@ViewScoped
@Named
public class AlterarSenhaMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private Pessoa pessoa = new Pessoa();
	private String senhaAntiga="";
	
	@Inject
	private PessoaDAO pessoaDAO;
	
	@Inject
	private PessoaService pessoaService;

	public AlterarSenhaMB() {
		criarNovoObjeto();

	}

	public void alterarSenha() {
		try {
			if (verificarEmail()) {
				pessoaService.atualizaSenha(CriptografiaSenha.criptografar(pessoa.getSenha()), pessoa.getEmail());
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Feito!!"));
			} else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Ops. Dados n�o conferem!!"));
			}
		} catch (Exception e) {

		}
		criarNovoObjeto();

	}

	public void criarNovoObjeto() {
		pessoa = new Pessoa();

	}

	public Boolean verificarEmail() {
		Boolean aux = false;
		List<Pessoa> pessoas = pessoaDAO.listaComStatus();
		String logado = SecurityContextHolder.getContext()
                .getAuthentication().getName();
		
		
		
		for (int i = 0; i < pessoas.size(); i++) {
			
			if (logado.equals(pessoas.get(i).getEmail()) && BCrypt.checkpw(senhaAntiga, pessoas.get(i).getSenha())) {
				aux = true;
				break;
			}
		}
		return aux;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getSenhaAntiga() {
		return senhaAntiga;
	}

	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}

}
