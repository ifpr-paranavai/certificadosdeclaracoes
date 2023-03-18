package br.com.cedi.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Pessoa;

@ViewScoped
@Named("usuarioSessaoMB")
public class UsuarioSessaoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa usuario = new Pessoa();
	String nomeUsuario = "";

	@Inject
	private PessoaDAO pessoaDAO;

	@PostConstruct
	public void init() {

		
		Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			System.out.println("Aquii usuario sessão");
			Object obj = authentication.getPrincipal();
			if (obj instanceof UserDetails) {
				nomeUsuario = ((UserDetails) obj).getUsername();
			} else {
				nomeUsuario = obj.toString();
			}
		}

		List<Pessoa> usu = pessoaDAO.listarCondicao("email='" + nomeUsuario + "'");
		if (usu.size() > 0) {
			usuario = usu.get(0);
		}

	}

	public Pessoa getUsuario() {
		return usuario;
	}

	public void setUsuario(Pessoa usuario) {
		this.usuario = usuario;
	}



}
