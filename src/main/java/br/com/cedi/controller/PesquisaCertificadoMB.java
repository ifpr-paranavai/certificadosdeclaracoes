package br.com.cedi.controller;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

import br.com.cedi.dao.CertificadoDAO;
import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Certificado;
import br.com.cedi.model.HistoricoBusca;
import br.com.cedi.model.Pessoa;
import br.com.cedi.service.CertificadoService;
import br.com.cedi.service.HistoricoBuscaService;
import br.com.cedi.service.PessoaService;
import br.com.cedi.util.utilitarios.ChamarRelatorio;
import br.com.cedi.util.utilitarios.ValidaCPF;

@ViewScoped
@Named
public class PesquisaCertificadoMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Certificado> listaCertificados = new ArrayList<>();
	private List<Certificado> listaCertificadosConsultaAluno = new ArrayList<>();
	private List<Certificado> listaCertificadosConsultaAlunoAutenticacao = new ArrayList<>();
	private Certificado certificadoSelecionado = new Certificado();

	private List<Pessoa> pessoasSelecionadas;

	private int activeIndex = 0;

	private String nome = new String("");
	private String cpf = new String("");
	private String cpfs = new String("");
	private String codigoAutenticacao = "";
	private String codigoCertificadoProvisorio="";

	@Inject
	private CertificadoDAO certificadoDAO;

	@Inject
	private HistoricoBuscaService historicoBuscaService;

	@Inject
	private CertificadoService certificadoService;
	
	@Inject
	private EntityManager manager;
	
	@Inject
	private PessoaDAO pessoaDAO;
	
	private String textoGerado = "";
	
	private Pessoa pessoa = new Pessoa();
	
	@Inject
	private PessoaService pessoaService;
	
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
		pessoa = new Pessoa();
	}
	

	public void chamarRelatorio(Certificado cs) {
		//System.out.println("Chamou o m�todo");
		
	

		// Para chamar um relatorio sempre vamos utilizar
		// esta classe ChamarRelatorio
		
		HashMap parametro = new HashMap<>();
		parametro.put("ID_CERTIFICADO", cs.getId());
		
		

		if (cs.getTipo().equalsIgnoreCase("CERTIFICADO") || cs.getTipo().trim().contains("MENÇÃO")) {
			ChamarRelatorio ch = new ChamarRelatorio("certificado.jasper", parametro, "certificado_" + String.valueOf(cs.getAutenticacao()));
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);
		} else {
			//ch.imprimeRelatorio("declaracao.jasper", parametro, "declaracao_" + String.valueOf(cs.getAutenticacao()));
			ChamarRelatorio ch = new ChamarRelatorio("declaracao.jasper", parametro, "declaracao_" + String.valueOf(cs.getAutenticacao()));
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);
		}
		cs.setImpresso(true);
		certificadoService.salvar(cs);

	}

	public void pesquisarCertificados() {
		//System.out.println("Dentro do pesquisar certificados");
		HistoricoBusca b = new HistoricoBusca();
		b.setCpf(cpf);
		b.setNome(nome);
		b.setTipo("pesquisa");
		b.setDataTarefa(new Date());
		historicoBuscaService.salvar(b);

		cpf = cpf.replace("'", "").replace("=", "");		
		nome = nome.replace("'", "").replace("=", "");
		String cpfLimpo = cpf.replace(".", "").replace("-", "");

		if (cpf.trim().length() > 0) {
			listaCertificadosConsultaAluno = certificadoDAO
					.listarCondicao("(pessoa.cpf='" + cpf + "' or pessoa.cpf='"+cpfLimpo+"') order by id desc");
			nome = "";
			cpf = "";
			if (listaCertificadosConsultaAluno.isEmpty()) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Ops. Nada encontrado!!"));
			}
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Informar o CPF!!"));
		}
	}

	public void pesquisarAutenticacao() {
		HistoricoBusca b = new HistoricoBusca();
		b.setCodigoValidacao(codigoAutenticacao);
		b.setTipo("autenticação");
		b.setDataTarefa(new Date());
		historicoBuscaService.salvar(b);

		codigoAutenticacao = codigoAutenticacao.replace("'", "").replace("=", "");

		if (codigoAutenticacao.trim().length() > 0) {
			listaCertificadosConsultaAlunoAutenticacao = certificadoDAO
					.listarCondicao("autenticacao='" + codigoAutenticacao.trim() + "'");
			codigoAutenticacao = "";
			if (listaCertificadosConsultaAlunoAutenticacao.isEmpty()) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Ops. Nada encontrado!!"));
			}
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Informar o código de autenticação!!"));
		}
	}
	
	public void incluirCertificadoDeclaracaoProvisoria(){
		textoGerado = "";
		cpf = cpf.replace("'", "").replace("=", "");		
		nome = nome.replace("'", "").replace("=", "");
		String cpfLimpo = cpf.replace(".", "").replace("-", "");

		if (codigoCertificadoProvisorio.trim().length() > 0 && cpf.trim().length() > 0) {
			List<Certificado> lc= certificadoDAO
					.listarCondicao("codigoCertificadoProvisorio = '" + codigoCertificadoProvisorio + "'");
			nome = "";
			cpf = "";
			if (lc.isEmpty()) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Ops. Nada encontrado!!"));
			}else{
				List<Pessoa> lp = pessoaDAO.listarCondicao("cpf='" + cpf + "' or cpf='"+cpfLimpo+"') order by id desc");
				if(lp.isEmpty()){
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage("Pessoa Não Encontrada!!"));
				}else{
					Certificado cert = lc.get(0);
					if(cert.getPessoa()==null){				
					Pessoa pes = lp.get(0);
					cert.setPessoa(pes);					
					cert.setTexto(cert.getTexto().replaceFirst(cert.getCodigoCertificadoProvisorio(), pes.getNome()));
					cert.setTexto(cert.getTexto().replaceFirst(cert.getCodigoCertificadoProvisorio()+"CPF", pes.getCpf()));
					cert.setCodigoCertificadoProvisorio(cert.getCodigoCertificadoProvisorio()+"-UTILIZADO");
					certificadoService.salvar(cert);
					textoGerado = cert.getTexto();
					
					codigoCertificadoProvisorio = "";
					cpf = "";
					
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage("Feito!! O certificado/declaração foi vinculado ao seu CPF e o código foi inutilizado!!"));
					}
					
				}
			}
			
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Código Incorreto!!"));
		}
		
		
	}
	
	

	public void onTabChange(TabChangeEvent event) {
		activeIndex = ((TabView) event.getSource()).getIndex();
		//System.out.println(activeIndex);
	}

	public void inserirCertificados2() {
		//System.out.println("dentro do 2");
	}

	private void preencheListaCertificados() {
		listaCertificados = certificadoDAO.listaComStatus();
		//System.out.println("Lista cert: " + listaCertificados.size());
	}

	public Certificado getCertificadoSelecionado() {
		return certificadoSelecionado;
	}

	public void setCertificadoSelecionado(Certificado certificadoSelecionado) {
		this.certificadoSelecionado = certificadoSelecionado;
	}

	public List<Pessoa> getPessoasSelecionadas() {
		return pessoasSelecionadas;
	}

	public void setPessoasSelecionadas(List<Pessoa> pessoasSelecionadas) {
		this.pessoasSelecionadas = pessoasSelecionadas;
	}

	public List<Certificado> getListaCertificados() {
		return listaCertificados;
	}

	public void setListaCertificados(List<Certificado> listaCertificados) {
		this.listaCertificados = listaCertificados;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public List<Certificado> getListaCertificadosConsultaAluno() {
		return listaCertificadosConsultaAluno;
	}

	public void setListaCertificadosConsultaAluno(List<Certificado> listaCertificadosConsultaAluno) {
		this.listaCertificadosConsultaAluno = listaCertificadosConsultaAluno;
	}

	public String getCpfs() {
		if (cpfs == null) {
			cpfs = "";
		}
		return cpfs;
	}

	public void setCpfs(String cpfs) {
		this.cpfs = cpfs;
	}

	public String getCodigoAutenticacao() {
		if (codigoAutenticacao == null) {
			codigoAutenticacao = "";
		}
		return codigoAutenticacao;
	}

	public void setCodigoAutenticacao(String codigoAutenticacao) {
		this.codigoAutenticacao = codigoAutenticacao;
	}

	public List<Certificado> getListaCertificadosConsultaAlunoAutenticacao() {
		return listaCertificadosConsultaAlunoAutenticacao;
	}

	public void setListaCertificadosConsultaAlunoAutenticacao(
			List<Certificado> listaCertificadosConsultaAlunoAutenticacao) {
		this.listaCertificadosConsultaAlunoAutenticacao = listaCertificadosConsultaAlunoAutenticacao;
	}

	public String getCodigoCertificadoProvisorio() {
		return codigoCertificadoProvisorio;
	}

	public void setCodigoCertificadoProvisorio(String codigoCertificadoProvisorio) {
		this.codigoCertificadoProvisorio = codigoCertificadoProvisorio;
	}

	public String getTextoGerado() {
		return textoGerado;
	}

	public void setTextoGerado(String textoGerado) {
		this.textoGerado = textoGerado;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	

}
