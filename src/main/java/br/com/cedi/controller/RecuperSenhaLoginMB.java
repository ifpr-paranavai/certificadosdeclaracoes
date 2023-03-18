package br.com.cedi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.cedi.dao.GenericDAO;
import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Pessoa;
import br.com.cedi.service.PessoaService;
import br.com.cedi.util.utilitarios.CriptografiaSenha;
import br.com.cedi.util.utilitarios.EnviarEmail;
import br.com.cedi.util.utilitarios.GeradorSenhas;


@ViewScoped
@Named("recuperSenhaLoginMB")
public class RecuperSenhaLoginMB implements Serializable{

	private static final long serialVersionUID = 1L;

	private String email;
	
	private List<Pessoa> lista;
	private String mensagem;
	
	@Inject
	private PessoaDAO daoPessoa;
	
	@Inject
	private PessoaService pessoaService;
	
	@PostConstruct
	public void inicializa() {
		lista = new ArrayList<>();
		mensagem = "";
		email = "";
	}
	public Boolean buscarEmail() {
		lista = daoPessoa.listaComStatus();
		System.out.println(lista.size());
		System.out.println(email);
		for(Pessoa p:lista){
			if(p.getEmail()!=null && p.getEmail().equalsIgnoreCase(email)){
				return true;
			}
		}
		return false;
	}

	public void recuperarSenhaLogin() {
		String senha;
		String novaSenha;
		if (buscarEmail()) {
			senha = GeradorSenhas.gerarSenha();
			novaSenha = senha.charAt(0)+""+senha.charAt(1)+""+senha.charAt(3)+""+senha.charAt(5)+""+senha.charAt(6);
			//System.out.println(novaSenha);
			boolean enviarEmail = false;
			pessoaService.atualizaSenha(CriptografiaSenha.criptografar(novaSenha.toLowerCase()), email);
			if (enviarEmail && EnviarEmail.enviarEmail(email, "Recuperação de Senha - Certificados e Declarações",
					"Olá,\nsua nova senha é: "+novaSenha.toLowerCase())) {
				mensagem = "E-mail enviado";
			} else {
				mensagem = "Erro ao enviar e-mail: "+novaSenha.toLowerCase();
			}
		} else {
			mensagem = "E-mail não encontrado";
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
