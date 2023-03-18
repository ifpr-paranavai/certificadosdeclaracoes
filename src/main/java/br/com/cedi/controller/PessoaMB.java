package br.com.cedi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Pessoa;
import br.com.cedi.service.PessoaService;
import br.com.cedi.util.utilitarios.CriptografiaSenha;
import br.com.cedi.util.utilitarios.ValidaCPF;

@ViewScoped
@Named
public class PessoaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa pessoa;
	private List<Pessoa> pessoas;
	private List<Pessoa> listaPessoasImportar;

	@Inject
	private PessoaService pessoaService;

	@Inject
	private PessoaDAO pessoaDAO;
	
	
	private String pessoasImportar;

	public void inicializar() {
		this.pessoa = new Pessoa();
		this.pessoas = new ArrayList<>();
		this.listaPessoasImportar = new ArrayList<>();
		preencherLista();
	}

	public void editarUsuario(Pessoa p) {
		System.out.println("Pessoa: " + p.getNome());
		this.pessoa = p;
	}
	
	public void prepararImportar(){
		//nome,cpf,email,ra/siape;
		this.listaPessoasImportar = new ArrayList<>();
		String[] pessoas = pessoasImportar.split(";");
		String cpfsInvalidos="";
		String pessoasCadastradas="";
		for(String p:pessoas){
			String[] atributos = p.split(",");
			Pessoa pes = new Pessoa();
			pes.setNome(atributos[0].toUpperCase());
			pes.setCpf(atributos[1].replace(".", "").replace("-", ""));
			pes.setEmail(atributos[2].toLowerCase());
			pes.setMatricula(atributos[3].toUpperCase());
			
			if(!ValidaCPF.isCPF(pes.getCpf())){
				cpfsInvalidos+= pes.getCpf()+"; ";
			}else{
				String cpfComPonto = pes.getCpf().trim().substring(0, 3)+"."+pes.getCpf().trim().substring(3, 6)+"."+pes.getCpf().trim().substring(6, 9)+"-"+pes.getCpf().trim().substring(9, 11);
				List<Pessoa> lp = pessoaDAO.listarCondicao("cpf='" + pes.getCpf().trim() + "' or cpf='"+pessoa.getCpf().replace(".", "").replace("-", "").trim()+"' or cpf='"+cpfComPonto+"'");
				if(lp.size()>0){
					pessoasCadastradas+=lp.get(0).getNome()+"-"+lp.get(0).getCpf()+"; ";
				}else{
					listaPessoasImportar.add(pes);
				}
			}
		}
		
		if(cpfsInvalidos.trim().length()>0){
			this.listaPessoasImportar = new ArrayList<>();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Existem CPFs Inválidos: "+cpfsInvalidos));
		}else if(pessoasCadastradas.trim().length()>0){
			//this.listaPessoasImportar = new ArrayList<>();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Algumas pessoas que estão na listas já estão cadastradas e não serão importadas: "+pessoasCadastradas));
		}
	}
	
	public void finalizarImportacao(){
		if(listaPessoasImportar.size()>0){
			for(Pessoa p:listaPessoasImportar){
				pessoaService.salvar(p);
			}
			this.listaPessoasImportar = new ArrayList<>();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Importação Finalizada!!"));
		}else{
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Nada para importar!!"));
		}
		
	}

	public void salvar() {
		try {
			
			if (pessoa.getId() == null) {
				pessoa.setStatus(true);
				pessoa.setTipo("pessoa");
				pessoa.setNome(pessoa.getNome().toUpperCase());
				// pessoa.setSenha(CriptografiaSenha.criptografar(pessoa.getSenha()));
				if (ValidaCPF.isCPF(pessoa.getCpf())) {
//					List<Pessoa> lp = pessoaDAO.listarCondicao("cpf='" + pessoa.getCpf().trim() + "'");
//					if (lp.size() > 0) {
//						FacesContext context = FacesContext.getCurrentInstance();
//						context.addMessage(null, new FacesMessage("CPF já cadastrado: " + lp.get(0).getNome()));
//					} else {
//						this.pessoaService.salvar(pessoa);
//					}
					if(verificaCadastrar()){
						this.pessoaService.salvar(pessoa);
					}

				} else {
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage("CPF Inválido!!"));
				}
				// FacesContext context = FacesContext.getCurrentInstance();
				// context.addMessage(null, new FacesMessage(Mensagem.SUCESSO));
			} else {
				// pessoa.setSenha(CriptografiaSenha.criptografar(pessoa.getSenha()));

				if (ValidaCPF.isCPF(pessoa.getCpf())) {
					pessoaService.salvar(pessoa);
				} else {
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage("CPF Inválido!!"));
				}
				// FacesContext context = FacesContext.getCurrentInstance();
				// context.addMessage(null, new FacesMessage(Mensagem.SUCESSO));
			}

		} catch (Exception e) {
			// FacesContext context = FacesContext.getCurrentInstance();
			// context.addMessage(null, new FacesMessage(Mensagem.ERRO));
		}
		criarNovoObjeto();
		preencherLista();
	}

	private boolean verificaCadastrar(){
		List<Pessoa> lp = pessoaDAO.listarCondicao("cpf='" + pessoa.getCpf().trim() + "' or cpf='"+pessoa.getCpf().replace(".", "").replace("-", "").trim()+"'");
		if (lp.size() > 0) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("CPF já cadastrado: " + lp.get(0).getNome()));
			return false;
		} else if(pessoa.getEmail().length()>2){
			
			lp = pessoaDAO.listarCondicao("email='" + pessoa.getEmail().trim() + "'");
			if (lp.size() > 0) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("E-mail já cadastrado: " + lp.get(0).getNome()));
				return false;
			} else {								
				return true;								
			}	
			
		}else{
			return true;
		}
		
	}

	public void atualizarUsuario() {
		if (pessoa.getId() != null) {
			try {
				pessoa.setCpf(pessoa.getCpf().replace(".", "").replace("-", "").trim());

				if (pessoa.getId() == null) {
					pessoa.setStatus(true);
					pessoa.setTipo("pessoa");
					pessoa.setSenha(CriptografiaSenha.criptografar(pessoa.getSenha()));
					if (ValidaCPF.isCPF(pessoa.getCpf())) {
						if(verificaCadastrar()){
							pessoaService.salvar(pessoa);
						}

					} else {
						FacesContext context = FacesContext.getCurrentInstance();
						context.addMessage(null, new FacesMessage("CPF Inválido!!"));
					}
					// FacesContext context = FacesContext.getCurrentInstance();
					// context.addMessage(null, new
					// FacesMessage(Mensagem.SUCESSO));
				} else {
					pessoa.setSenha(CriptografiaSenha.criptografar(pessoa.getSenha()));
					if (pessoa.getUsuarioSistema()) {
						pessoa.setTipo("pessoa");
					} else {
						pessoa.setTipo("");
						pessoa.setSenha("");
					}

					if (ValidaCPF.isCPF(pessoa.getCpf())) {
						pessoaService.salvar(pessoa);
					} else {
						FacesContext context = FacesContext.getCurrentInstance();
						context.addMessage(null, new FacesMessage("CPF Inválido!!"));
					}

				}

			} catch (Exception e) {			
			}
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Somente alteração!!!"));
		}
		criarNovoObjeto();
		preencherLista();

	}

	public void inativar(Pessoa p) {
		try {
			System.out.println("No inativar");
			p.setStatus(false);
			p.setCpf(p.getCpf() + " inativo");
			pessoaService.salvar(p);
			preencherLista();
			// FacesContext context = FacesContext.getCurrentInstance();
			// context.addMessage(null, new FacesMessage(Mensagem.SUCESSO));
		} catch (Exception e) {
			// FacesContext context = FacesContext.getCurrentInstance();
			// context.addMessage(null, new FacesMessage(Mensagem.ERRO));
		}
	}

	public void criarNovoObjeto() {
		pessoa = new Pessoa();

	}

	public void preencherLista() {
		pessoas = pessoaDAO.listaComStatus();
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public String getPessoasImportar() {
		return pessoasImportar;
	}

	public void setPessoasImportar(String pessoasImportar) {
		this.pessoasImportar = pessoasImportar;
	}

	public List<Pessoa> getListaPessoasImportar() {
		return listaPessoasImportar;
	}

	public void setListaPessoasImportar(List<Pessoa> listaPessoasImportar) {
		this.listaPessoasImportar = listaPessoasImportar;
	}
	
	

}
