package br.com.cedi.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.primefaces.component.tabview.TabView;
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
import br.com.cedi.util.utilitarios.ChamarRelatorio;
import br.com.cedi.util.utilitarios.RetornaLogado;

@ViewScoped
@Named
public class ListaCertificadosMB implements Serializable{
	private static final long serialVersionUID = 1L;
		
	private List<Certificado> listaCertificados;

	private Long lote=0l;

	@Inject
	private CertificadoDAO certificadoDAO;
	
	@Inject
	RetornaLogado logado;


	@Inject
	private CertificadoService certificadoService;

		
	
	@Inject
	private EntityManager manager;

	@PostConstruct
	public void inicializar(){	
		System.out.println("No inicializar Lista Certificados");
		listaCertificados = new ArrayList<>();
		preencheListaCertificados();
	}

	public void inativarCertificado(Certificado p) {
		try {
			System.out.println("No inativar");
			p.setStatus(false);
			certificadoService.salvar(p);
			preencheListaCertificados();
			// FacesContext context = FacesContext.getCurrentInstance();
			// context.addMessage(null, new FacesMessage(Mensagem.SUCESSO));
		} catch (Exception e) {
			// FacesContext context = FacesContext.getCurrentInstance();
			// context.addMessage(null, new FacesMessage(Mensagem.ERRO));
		}
	}
	
	public void imprimirCodigosProvisorios() {

		List<Certificado> lcc = certificadoDAO.listarCondicao("loteCertificado=" + getLote());

		System.out.println(lcc.size());
		if (lcc.size() > 0) {
			Certificado cs = lcc.get(0);

			HashMap parametro = new HashMap<>();
			parametro.put("TITULO_EVENTO", cs.getTituloEvento());
			parametro.put("LOTE", cs.getLoteCertificado());

			ChamarRelatorio ch = new ChamarRelatorio("relacao_codigos_provisorios.jasper", parametro,
					"relacao_codigos_provisorios" + cs.getLoteCertificado());
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);

		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Ops. Nada encontrado!!"));
		}

	}
	


	public void imprimirRelacaoCertificado() {
		List<Certificado> lcc = certificadoDAO.listarCondicao("loteCertificado=" + getLote());

		System.out.println(lcc.size());
		if (lcc.size() > 0) {
			Certificado cs = lcc.get(0);

			HashMap parametro = new HashMap<>();
			parametro.put("TITULO_EVENTO", cs.getTituloEvento());
			parametro.put("LOTE", cs.getLoteCertificado());
			
			ChamarRelatorio ch = new ChamarRelatorio("relacao_certificados.jasper", parametro, "relacao_certificados_" + cs.getLoteCertificado());
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);
		
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Ops. Nada encontrado!!"));
		}
		
		lote = 0L;
	}
	
	public void excluirLoteCertificado() {
		List<Certificado> lcc = certificadoDAO.listarCondicao("loteCertificado=" + getLote());

		System.out.println(lcc.size());
		if (lcc.size() > 0) {
			for(Certificado c:lcc){
				c.setStatus(false);
				certificadoService.salvar(c);				
			}
			preencheListaCertificados();
		
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Ops. Nada encontrado!!"));
		}
		lote = 0L;

	}	


	private void preencheListaCertificados() {
		System.out.println("ID da Pessoa: "+logado.getPessoaLogada().getId());		
		
		listaCertificados = certificadoDAO.listarCondicao("responsavelCadastro.id="+ logado.getPessoaLogada().getId()+" order by id desc");
		System.out.println("Lista cert: " + listaCertificados.size());		
	}


	public List<Certificado> getListaCertificados() {
		return listaCertificados;
	}

	public void setListaCertificados(List<Certificado> listaCertificados) {
		this.listaCertificados = listaCertificados;
	}




	public Long getLote() {
		return lote;
	}




	public void setLote(Long lote) {
		this.lote = lote;
	}
	
	

}
