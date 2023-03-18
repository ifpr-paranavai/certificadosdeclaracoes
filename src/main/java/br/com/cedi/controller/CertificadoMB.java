package br.com.cedi.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.cedi.dao.CertificadoDAO;
import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Atividade;
import br.com.cedi.model.Certificado;
import br.com.cedi.model.Historico;
import br.com.cedi.model.LoteCertificados;
import br.com.cedi.model.Pessoa;
import br.com.cedi.service.AtividadeService;
import br.com.cedi.service.CertificadoService;
import br.com.cedi.service.HistoricoService;
import br.com.cedi.service.LoteCertificadoService;
import br.com.cedi.service.PessoaService;
import br.com.cedi.util.utilitarios.ChamarRelatorio;
import br.com.cedi.util.utilitarios.EnviarEmailThread;
import br.com.cedi.util.utilitarios.GeradorCodigosProvisorios;
import br.com.cedi.util.utilitarios.RetornaLogado;
import br.com.cedi.util.utilitarios.ValidaCPF;

@ViewScoped
@Named
public class CertificadoMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Certificado> listaCertificadosProvisorio;
	private List<Certificado> listaCertificados;
	private List<Certificado> listaCertificadosConsultaAluno;
	private List<Certificado> listaCertificadosConsultaAlunoAutenticacao;
	private List<String> listaAtividadesComplementares;

	@Inject
	private Certificado certificadoSelecionado;
	@Inject
	private Certificado certificadoSelecionadoEditar;
	private List<Pessoa> listaPessoas;
	private List<Pessoa> pessoasSelecionadas;

	@Inject
	private CertificadoDAO certificadoDAO;

	@Inject
	private PessoaDAO pessoaDAO;

	@Inject
	private CertificadoService certificadoService;

	@Inject
	private PessoaService pessoaService;

	@Inject
	private LoteCertificadoService loteCertificadoService;

	@Inject
	private AtividadeService atividadeService;

	@Inject
	private HistoricoService historicoService;

	private String descricaoAtividade = "";
	private String dataAtividade = "";

	// private List<ResponsaveisAssinaturas> listaResponsaveisAssinaturas = new
	// ArrayList<>();
	// private ResponsaveisAssinaturas responsaveisAssintaurasSelecionado = new
	// ResponsaveisAssinaturas();

	private List<Atividade> listaAtividades;
	private Atividade atividadeSelecionada;
	private int activeIndex;

	private String nome;

	private String cpf;

	private String cpfs;
	private String dadosEven;
	private String codigoAutenticacao;
	private Long lote;
	// private Pessoa pessoaLogada = pegarPessoaLogada();
	private Pessoa pessoaLogada;

	private Integer quantidadeCertificadosProvisorios;

	@Inject
	private EntityManager manager;

	private boolean mostrarTabelaCertificados = false;

	private Pessoa pessoaSelecionada;

	public void inicializar() {

		nome = new String("");
		cpf = new String("");
		cpfs = new String("");
		codigoAutenticacao = "";
		listaAtividades = new ArrayList<>();
		atividadeSelecionada = new Atividade();
		activeIndex = 0;
		listaCertificadosProvisorio = new ArrayList<>();
		listaCertificados = new ArrayList<>();
		listaCertificadosConsultaAluno = new ArrayList<>();
		listaCertificadosConsultaAlunoAutenticacao = new ArrayList<>();
		certificadoSelecionado = new Certificado();
		pessoasSelecionadas = new ArrayList<>();

		//preencheListaPessoas();
		pessoasSelecionadas = new ArrayList<>();
		//preencheListaCertificados();
		mostrarTabelaCertificados = false;
	}
	
	public List<Pessoa> buscarPessoaNomeCpf(String nomeCpf) {
		nomeCpf = nomeCpf.replace(".", "").replace(".", "").replace(".", "").replace("-", "");
		
		return pessoaDAO
				.listarCondicao("nome like '%"+nomeCpf+"%' or cpf like '%"+nomeCpf+"%'");
		//System.out.println("LISTA "+listaPessoas.size());
	}

	public void carregarDadosEven() {
		pessoasSelecionadas = new ArrayList<>();
		cpfs = "<b>CPFs incorretos</b><br/>";
		String[] linhas = dadosEven.split("\n");
		// Descrição da Atividade, Nome, CPF e Data da Atividade (Data:
		// dd/MM/yyyy)
		for (int x = 0; x < linhas.length; x++) {

			String[] separados = linhas[x].split("\t");

			if (separados.length > 1) {

				String nome = separados[0];
				String cpf = separados[1];
				String email = null;
				if (separados.length == 3) {
					email = separados[2];
				}
				List<Pessoa> lp = buscaPessoaCpf(cpf);
				Pessoa p = new Pessoa();
				if (lp.size() > 0) {
					// System.out.println("Tamanho Pessoa: " + lp.size());
					p = lp.get(0);
					// pessoasSelecionadas.add(p);
					adicionarPessoaSelecionada(p);
				} else {
					if (ValidaCPF.isCPF(cpf)) {
						// System.out.println("Nova Pessoa");

						p.setNome(nome.toUpperCase());
						p.setCpf(cpf);
						p.setEmail(email);
						// pessoasSelecionadas.add(p);
						adicionarPessoaSelecionada(p);
					} else {
						cpfs += "<b>Nome:</b> " + nome + " - <b>CPF:</b> " + cpf + "<br/>";
					}
				}

		
			}

		}
		dadosEven = "";
		if(cpfs.equals("<b>CPFs incorretos</b><br/>")) {
			cpfs="";
		}else {
			cpfs+="<br/>";
		}
		// System.out.println(cpfs);
	}

	public static String getLogado() {
		String logado = SecurityContextHolder.getContext().getAuthentication().getName();
		return logado;
	}

	private br.com.cedi.model.Pessoa pegarPessoaLogada() {

		Pessoa p = null;
		List<Pessoa> lp = pessoaDAO.listarCondicao("email like '%" + getLogado() + "%'");
		if (lp.size() > 0) {
			p = lp.get(0);
		}
		return p;
	}

	public void editarCertificado(Certificado cer) {
		this.certificadoSelecionadoEditar = cer;
	}

	public void finalizarEdicao() {
		// System.out.println("Edição concluída!");
	}

	public void finalizarEdicaoLista() {
		certificadoService.salvar(certificadoSelecionadoEditar);
		certificadoSelecionadoEditar = null;
		preencheListaCertificados();
		// System.out.println("Edição concluída!");
	}

	// @Inject
	// PessoaDAO pessoaDAO;

	public void pesquisarPessoas() {
		pessoasSelecionadas = new ArrayList<>();
	
		//preencheListaPessoas();
	}

	public void adicionarPessoa() {
		if (pessoaSelecionada != null) {
			// pessoasSelecionadas.add(pessoaSelecionada);
			adicionarPessoaSelecionada(pessoaSelecionada);
		}
		pessoaSelecionada = null;
	}

	private void adicionarPessoaSelecionada(Pessoa p) {
		if (!verificaPessoaListaCertificados(p)) {

			Certificado cert = new Certificado();
			cert.setCargaHoraria(certificadoSelecionado.getCargaHoraria());
			cert.setDataEmissao(certificadoSelecionado.getDataEmissao());
			cert.setNumeroFolha(certificadoSelecionado.getNumeroFolha());
			cert.setNumeroLivro(certificadoSelecionado.getNumeroLivro());
			cert.setNumeroRegistro(certificadoSelecionado.getNumeroRegistro());
			cert.setTituloEvento(certificadoSelecionado.getTituloEvento());
			cert.setPessoa(p);
			cert.setPorcentagemFrequencia(certificadoSelecionado.getPorcentagemFrequencia());
			listaCertificadosProvisorio.add(cert);
		}
	}

	public void pesquisarCpf() {
	
		String[] listaCpf = cpfs.split("\n");
		cpfs = "";
		listaPessoas = new ArrayList<>();
		for (String cp : listaCpf) {

			List<Pessoa> lp = buscaPessoaCpf(cp.trim());
		
			if (lp.size() > 0) {
				// pessoasSelecionadas.add(lp.get(0));
				adicionarPessoaSelecionada(lp.get(0));
			} else {
				cpfs += cp + "\n";
			}
		}
	}

	public List<Pessoa> buscaPessoaCpf(String cp) {
		String cpfMascara = "";
		String cpfLimpo = cp.replace(".", "").replace(".", "").replace(".", "").replace(".", "").replace("-", "");

		if (cp.trim().length() > 10) {
			cpfMascara = cpfLimpo.trim().substring(0, 3) + "." + cpfLimpo.trim().substring(3, 6) + "."
					+ cpfLimpo.trim().substring(6, 9) + "-" + cpfLimpo.trim().substring(9, 11);
		}

		List<Pessoa> lp = pessoaDAO
				.listarCondicao("cpf='" + cp.trim() + "' or cpf='" + cpfMascara + "'" + " or cpf='" + cpfLimpo + "'");
		return lp;
	}

	public void chamarRelatorio(Certificado cs) {
		// System.out.println("Chamou o m�todo");

		// Para chamar um relatorio sempre vamos utilizar
		// esta classe ChamarRelatorio

		HashMap parametro = new HashMap<>();
		parametro.put("ID_CERTIFICADO", cs.getId());

		if (cs.getTipo().equalsIgnoreCase("CERTIFICADO") || cs.getTipo().trim().contains("MENÇÃO")) {
			ChamarRelatorio ch = new ChamarRelatorio("certificado.jasper", parametro,
					"certificado_" + String.valueOf(cs.getAutenticacao()));
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);
		} else {
			// ch.imprimeRelatorio("declaracao.jasper", parametro, "declaracao_"
			// + String.valueOf(cs.getAutenticacao()));
			ChamarRelatorio ch = new ChamarRelatorio("declaracao.jasper", parametro,
					"declaracao_" + String.valueOf(cs.getAutenticacao()));
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);
		}
		cs.setImpresso(true);
		certificadoService.salvar(cs);

	}

	public void imprimirRelacaoCertificado() {

		List<Certificado> lcc = certificadoDAO.listarCondicao("loteCertificado=" + getLote());

		// System.out.println(lcc.size());
		if (lcc.size() > 0) {
			Certificado cs = lcc.get(0);

			HashMap parametro = new HashMap<>();
			parametro.put("TITULO_EVENTO", cs.getTituloEvento());
			parametro.put("LOTE", cs.getLoteCertificado());

			ChamarRelatorio ch = new ChamarRelatorio("relacao_certificados.jasper", parametro,
					"relacao_certificados_" + cs.getLoteCertificado());
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);

		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Ops. Nada encontrado!!"));
		}

	}

	public void pesquisarCertificados() {
		cpf = cpf.replace("'", "").replace("=", "");
		nome = nome.replace("'", "").replace("=", "");

		if (nome.trim().length() > 0 && cpf.trim().length() > 0) {
			listaCertificadosConsultaAluno = certificadoDAO
					.listarCondicao("pessoa.nome like '%" + nome + "%' and pessoa.cpf='" + cpf + "' order by impresso");
			nome = "";
			cpf = "";
			if (listaCertificadosConsultaAluno.isEmpty()) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Ops. Nada encontrado!!"));
			}
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Informar o Nome e CPF!!"));
		}
	}

	public void pesquisarAutenticacao() {
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
			context.addMessage(null, new FacesMessage("Informar o c�digo de autentica��o!!"));
		}
	}

	public void onTabChange(TabChangeEvent event) {
		activeIndex = ((TabView) event.getSource()).getIndex();
		// System.out.println(activeIndex);
	}

	public void inserirCertificados2() {
		// System.out.println("dentro do 2");
	}

	public void inserirCertificadosProvisorios() {
		String textoFinal = "";
		textoFinal += certificadoSelecionado.getTextoInicial() + " " + certificadoSelecionado.getTextoFinal();
		for (Certificado cer : listaCertificadosProvisorio) {
			String nome = "";
			if (certificadoSelecionado.getMostrarCpf()) {
				if (cer.getPessoa() != null) {
					nome = "<b>" + cer.getPessoa().getNome() + "</b>" + ", inscrito(a) no CPF " + cer.getPessoa().getCpf().trim();
				} else {
					nome = "<b>" + cer.getCodigoCertificadoProvisorio() + "</b>" + ", inscrito(a) no CPF "
							+ cer.getCodigoCertificadoProvisorio() + "CPF";
				}

			} else {
				if (cer.getPessoa() != null) {
					nome = "<b>" + cer.getPessoa().getNome() + "</b>";
				} else {
					nome = "<b>" + cer.getCodigoCertificadoProvisorio() + "</b>";
				}
			}
			cer.setQuantidadeHoras(certificadoSelecionado.getQuantidadeHoras());
			cer.setTexto(certificadoSelecionado.getTextoInicial() + " " + nome + " "
					+ certificadoSelecionado.getTextoFinal());
			cer.setTexto(cer.getTexto().replace("$horas", String.valueOf(cer.getPessoa().getQuantidadeHoras())));
		}
		mostrarTabelaCertificados = true;
	}

	public String inserirCertificados() {
		// Para salvar um certificado tem que adicionar o texto completo
		// Setar o tipo
		// que é o texto inicial + nome + texto final.
		// após salvar o certificado tem que adicionar seu objeto na lista de
		// responsáveis e salva-los
		// também adiciona-lo na lista de atividades e salva-las.

		Boolean confirmar = true;

		String textoFinal = "";
		textoFinal += certificadoSelecionado.getTextoInicial() + " " + certificadoSelecionado.getTextoFinal();
		if (textoFinal.contains("font-family")) {
			confirmar = false;
		}

		if (confirmar) {
			// System.out.println("Na lista de Certificados....");

			LoteCertificados lote = new LoteCertificados();
			loteCertificadoService.salvar(lote);

			for (Certificado cer : listaCertificadosProvisorio) {

				if (cer.getPessoa().getId() == null) {
					pessoaService.salvar(cer.getPessoa());
				}

				cer.setAtividadeComplementar(certificadoSelecionado.getAtividadeComplementar().replace("G1-", "")
						.replace("G2-", "").replace("G3-", ""));
				cer.setTipo(certificadoSelecionado.getTipo());
				cer.setInformacoesAdicionais(certificadoSelecionado.getInformacoesAdicionais());
				cer.setStatus(true);
				cer.setLoteCertificado(lote.getId());
				cer.setResponsavelAssinatura1(certificadoSelecionado.getResponsavelAssinatura1());
				cer.setPapelAssinatura1(certificadoSelecionado.getPapelAssinatura1());
				cer.setResponsavelAssinatura2(certificadoSelecionado.getResponsavelAssinatura2());
				cer.setPapelAssinatura2(certificadoSelecionado.getPapelAssinatura2());
				cer.setResponsavelAssinatura3(certificadoSelecionado.getResponsavelAssinatura3());
				cer.setPapelAssinatura3(certificadoSelecionado.getPapelAssinatura3());
				// cer.setQuantidadeHoras(certificadoSelecionado.getQuantidadeHoras());

				cer.setPapelResponsavelCadastro(certificadoSelecionado.getPapelResponsavelCadastro());

				certificadoService.salvar(cer);

				cer.setAutenticacao(
						String.valueOf(String.valueOf(cer.getId()) + "." + String.valueOf(cer.getLoteCertificado())));

				cer.setNumeroRegistro(cer.getId());
				cer.setCadastradoPor(RetornaLogado.getLogado());
				cer.setResponsavelCadastro(pegarPessoaLogada());

				certificadoService.salvar(cer);

				if (cer.getPessoa().getEmail().trim().length() > 7) {
					Thread t = new Thread(new EnviarEmailThread(cer.getPessoa().getEmail(),
							(cer.getTipo().equals("CERTIFICADO") ? "Novo "
									: "Nova ") + cer.getTipo() + " - Sistema de Certificados e Declarações",
							"Olá, " + cer.getPessoa().getNome() + ". Foi gerado " + (cer.getTipo().equals("CERTIFICADO")
									? "um novo "
									: "uma nova ") + cer.getTipo()
											+ " para você. Acesse pelo seguinte link para gerar um PDF do documento: https://certificados.paranavai.ifpr.edu.br/. <br/><br/>"
											+ cer.getTexto()));
					t.start();
				}

				for (Atividade ati : listaAtividades) {
					Atividade ac = new Atividade();
					ac.setAtividade(ati.getAtividade());
					ac.setCargaHoraria(ati.getCargaHoraria());
					ac.setCertificado(cer);
					ac.setResponsavel(ati.getResponsavel());

					atividadeService.salvar(ac);
				}
				if (listaAtividades.size() > 0) {
					cer.setAtividadesVerso(true);
					certificadoService.salvar(cer);
				}

				Historico his = new Historico();
				his.setLote(String.valueOf(lote.getId()) + "-" + String.valueOf(lote.getDescricao()));
				his.setPessoaCertificada(cer.getPessoa());
				his.setPessoaLogada(cer.getResponsavelCadastro());
				his.setInformacoes(cer.getTexto());
				historicoService.salvar(his);

			}

			preencheListaCertificados();
			// System.out.println("Antes do RETURN");
			return "listaCertificados?faces-redirect=true";
		} else {
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('myDialogVar').show();");
		}
		return "";
	}

	public void selecionado() {
		// System.out.println("Selecionado");
	}

	public void adicionarCertificados() {

		for (Pessoa p : pessoasSelecionadas) {
			if (!verificaPessoaListaCertificados(p)) {

				Certificado cert = new Certificado();
				cert.setCargaHoraria(certificadoSelecionado.getCargaHoraria());
				cert.setDataEmissao(certificadoSelecionado.getDataEmissao());
				cert.setNumeroFolha(certificadoSelecionado.getNumeroFolha());
				cert.setNumeroLivro(certificadoSelecionado.getNumeroLivro());
				cert.setNumeroRegistro(certificadoSelecionado.getNumeroRegistro());
				cert.setTituloEvento(certificadoSelecionado.getTituloEvento());
				cert.setPessoa(p);
				cert.setPorcentagemFrequencia(certificadoSelecionado.getPorcentagemFrequencia());
				listaCertificadosProvisorio.add(cert);
			}
		}
		preencheListaPessoas();
	}

	public void adicionarCertificadosProvisorios() {

		if (quantidadeCertificadosProvisorios != null && quantidadeCertificadosProvisorios > 0) {
			for (int x = 0; x < quantidadeCertificadosProvisorios; x++) {
				Certificado cert = new Certificado();
				cert.setCargaHoraria(certificadoSelecionado.getCargaHoraria());
				cert.setDataEmissao(certificadoSelecionado.getDataEmissao());
				cert.setNumeroFolha(certificadoSelecionado.getNumeroFolha());
				cert.setNumeroLivro(certificadoSelecionado.getNumeroLivro());
				cert.setNumeroRegistro(certificadoSelecionado.getNumeroRegistro());
				cert.setTituloEvento(certificadoSelecionado.getTituloEvento());
				// cert.setPessoa(p);
				cert.setCodigoCertificadoProvisorio(GeradorCodigosProvisorios.gerarCodigo());
				cert.setPorcentagemFrequencia(certificadoSelecionado.getPorcentagemFrequencia());
				listaCertificadosProvisorio.add(cert);
			}
			preencheListaPessoas();
		}
	}

	public void removerCertificadosProvisorios(Certificado cer) {
		listaCertificadosProvisorio.remove(cer);
	}

	public void adicionaAtividades() {
		listaAtividades.add(atividadeSelecionada);
		atividadeSelecionada = new Atividade();
	}

	public void removerAtividade(Atividade ra) {
		listaAtividades.remove(ra);
	}

	private boolean verificaPessoaListaCertificados(Pessoa p) {
		for (Certificado c : listaCertificadosProvisorio) {
			if (c.getPessoa() != null && c.getPessoa().equals(p)) {
				return true;
			}
		}
		return false;
	}

	public void corrigir() {
		List<Pessoa> lp = pessoaDAO.listaComStatus();
		for (Pessoa p : lp) {
			p.setNome(p.getNome().trim());
			this.pessoaService.salvar(p);
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Feito!!"));
	}

	private void preencheListaPessoas() {
		//listaPessoas = pessoaDAO.listaComStatus();
		pessoasSelecionadas = new ArrayList<>();

		// if (listaPessoas.size() < 1) {
		// Pessoa pp = new Pessoa();
		// pp.setEmail("frankwco@gmail.com");
		// pp.setNome("Frank Willian Cardoso de Oliveira");
		// pp.setCpf("04429615969");
		// pp.setStatus(true);
		// pp.setTipo("pessoa");
		// pp.setSenha(CriptografiaSenha.criptografar("gwh28"));
		//
		// dao.inserir(pp);
		//
		// }
	}

	private void preencheListaCertificados() {
		listaCertificados = certificadoDAO.listaComStatus();
		// System.out.println("Lista cert: " + listaCertificados.size());

	}

	public List<Certificado> getListaCertificadosProvisorio() {
		return listaCertificadosProvisorio;
	}

	public void setListaCertificadosProvisorio(List<Certificado> listaCertificadosProvisorio) {
		this.listaCertificadosProvisorio = listaCertificadosProvisorio;
	}

	public Certificado getCertificadoSelecionado() {
		return certificadoSelecionado;
	}

	public void setCertificadoSelecionado(Certificado certificadoSelecionado) {
		this.certificadoSelecionado = certificadoSelecionado;
	}

	public List<Pessoa> getListaPessoas() {
		return listaPessoas;
	}

	public void setListaPessoas(List<Pessoa> listaPessoas) {
		this.listaPessoas = listaPessoas;
	}

	public List<Pessoa> getPessoasSelecionadas() {
		return pessoasSelecionadas;
	}

	public void setPessoasSelecionadas(List<Pessoa> pessoasSelecionadas) {
		this.pessoasSelecionadas = pessoasSelecionadas;
	}

	public List<Atividade> getListaAtividades() {
		return listaAtividades;
	}

	public void setListaAtividades(List<Atividade> listaAtividades) {
		this.listaAtividades = listaAtividades;
	}

	public Atividade getAtividadeSelecionada() {
		return atividadeSelecionada;
	}

	public void setAtividadeSelecionada(Atividade atividadeSelecionada) {
		this.atividadeSelecionada = atividadeSelecionada;
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

	public Long getLote() {
		if (lote == null) {
			lote = 0L;
		}
		return lote;
	}

	public void setLote(Long lote) {
		this.lote = lote;
	}

	public Pessoa getPessoaLogada() {
		return pessoaLogada;
	}

	public void setPessoaLogada(Pessoa pessoaLogada) {
		this.pessoaLogada = pessoaLogada;
	}

	public boolean isMostrarTabelaCertificados() {
		return mostrarTabelaCertificados;
	}

	public void setMostrarTabelaCertificados(boolean mostrarTabelaCertificados) {
		this.mostrarTabelaCertificados = mostrarTabelaCertificados;
	}

	public Certificado getCertificadoSelecionadoEditar() {
		return certificadoSelecionadoEditar;
	}

	public void setCertificadoSelecionadoEditar(Certificado certificadoSelecionadoEditar) {
		this.certificadoSelecionadoEditar = certificadoSelecionadoEditar;
	}

	public Integer getQuantidadeCertificadosProvisorios() {
		return quantidadeCertificadosProvisorios;
	}

	public void setQuantidadeCertificadosProvisorios(Integer quantidadeCertificadosProvisorios) {
		this.quantidadeCertificadosProvisorios = quantidadeCertificadosProvisorios;
	}

	public String getDadosEven() {
		return dadosEven;
	}

	public void setDadosEven(String dadosEven) {
		this.dadosEven = dadosEven;
	}

	public List<String> getListaAtividadesComplementares() {
		if (listaAtividadesComplementares == null) {
			listaAtividadesComplementares = new ArrayList<>();
			listaAtividadesComplementares.add("G1-Estágio Supervisionado (Não Obrigatório)");
			listaAtividadesComplementares.add("G1-Monitoria");
			listaAtividadesComplementares.add("G1-Aula magna");
			listaAtividadesComplementares.add("G1-Palestras em áreas correlatas ao curso");
			listaAtividadesComplementares.add("G1-Fóruns e seminários em áreas correlatas ao curso");
			listaAtividadesComplementares.add("G1-Conferências e Congressos em áreas correlatas ao curso");
			listaAtividadesComplementares.add("G1-Debates em áreas correlatas ao curso");
			listaAtividadesComplementares.add("G1-Encontros em áreas correlatas ao curso");
			listaAtividadesComplementares.add("G1-Jornadas acadêmicas");
			listaAtividadesComplementares.add("G1-Simpósios em áreas correlatas ao curso");
			listaAtividadesComplementares.add("G1-Visitas monitoradas realizas pelo IFPR");
			listaAtividadesComplementares.add("G1-Atividades de campo");
			listaAtividadesComplementares.add("G1-Outros cursos técnicos ou de graduação em áreas correlatas ao curso");
			listaAtividadesComplementares.add("G1-Curso de qualificação em áreas correlatas ao curso - EAD");
			listaAtividadesComplementares.add("G1-Curso de qualificação em áreas correlatas ao curso - Semipresencial");
			listaAtividadesComplementares.add("G1-Curso de qualificação em áreas correlatas ao curso - Presencial");
			listaAtividadesComplementares.add("G1-Participação em projetos de ensino em áreas correlatas ao curso");
			listaAtividadesComplementares.add("G1-Participação em grupos de estudos em áreas correlatas ao curso");
			listaAtividadesComplementares.add("G1-Olimpíadas do conhecimento em áreas correlatas ao curso");
			listaAtividadesComplementares.add("G1-Outras Atividades de Ensino");
			listaAtividadesComplementares.add("G2-Participação em programas de bolsas Institucionais");
			listaAtividadesComplementares
					.add("G2-Participação em programas de bolsas ofertadas por Agências de Fomento");
			listaAtividadesComplementares
					.add("G2-Participação em Projetos de Iniciação Científicas, relacionados com objetivo do curso");
			listaAtividadesComplementares
					.add("G2-Participação como colaborador em projetos de Pesquisa, Extensão e Inovação");
			listaAtividadesComplementares.add(
					"G2-Participação como apresentador de trabalhos em palestras, congressos, seminários e mini cursos");
			listaAtividadesComplementares.add("G2-Participação como expositor em exposições técnico-científicas");
			listaAtividadesComplementares
					.add("G2-Participação efetiva na organização de exposições e seminários de caráter acadêmico");
			listaAtividadesComplementares.add("G2-Publicações em revistas técnicas");
			listaAtividadesComplementares.add(
					"G2-Publicações em anais de eventos técnico-científicos ou em periódicos científicos de abrangência local, regional, nacional ou internacional");
			listaAtividadesComplementares.add("G2-Livro ou capítulo de livros publicados");
			listaAtividadesComplementares.add("G2-Participação em grupos de pesquisa");
			listaAtividadesComplementares
					.add("G2-Participação em Empresa Junior, Hotel Tecnológico, Incubadora Tecnológica");
			listaAtividadesComplementares.add("G2-Participação em projetos multidisciplinares ou interdisciplinares");
			listaAtividadesComplementares.add("G2-Outras Atividades de Pesquisa, Extensão e Inovação");
			listaAtividadesComplementares.add("G3-Participação em atividades esportivas");
			listaAtividadesComplementares.add("G3-Participação em cursos de línguas estrangeiras");
			listaAtividadesComplementares.add(
					"G3-Participação em atividades artísticas e culturais, tais como: banda marcial, camerata de sopro, teatro, coral, radioamadorismo e outras");
			listaAtividadesComplementares.add(
					"G3-Participação efetiva na organização de exposições e seminários de caráter artísticos ou cultural");
			listaAtividadesComplementares.add("G3-Participação como expositor em exposição artística ou cultural");
			listaAtividadesComplementares.add(
					"G3-Participação efetiva em Diretórios e Centros Acadêmicos, Entidades de Classe e Colegiados internos à Instituição");
			listaAtividadesComplementares.add(
					"G3-Participação efetiva em trabalho voluntário, atividades comunitárias, CIPAS, associações de bairros, brigadas de incêndio e associações escolares");
			listaAtividadesComplementares.add("G3-Participação em atividades beneficentes");
			listaAtividadesComplementares.add(
					"G3-Atuação como instrutor em palestras técnicas, seminários, cursos da área específica, desde que não remunerados");
			listaAtividadesComplementares
					.add("G3-Engajamento como docente não remunerado em cursos preparatórios e de reforço escolar");
			listaAtividadesComplementares
					.add("G3-Participação em projetos de extensão, não remunerados, e de interesse social");
			listaAtividadesComplementares.add("G3-Serviço eleitoral obrigatório");
			listaAtividadesComplementares.add("G3-Outras Atividades de Formação Social, Humana e Cultural");
		}
		return listaAtividadesComplementares;
	}

	public void setListaAtividadesComplementares(List<String> listaAtividadesComplementares) {
		this.listaAtividadesComplementares = listaAtividadesComplementares;
	}

	public Pessoa getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(Pessoa pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}

}
