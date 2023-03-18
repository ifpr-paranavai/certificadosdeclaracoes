package br.com.cedi.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.cedi.dao.CertificadoDAO;
import br.com.cedi.dao.HistoricoBuscaDAO;
import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Atividade;
import br.com.cedi.model.Certificado;
import br.com.cedi.model.Historico;
import br.com.cedi.model.HistoricoBusca;
import br.com.cedi.model.LoteCertificados;
import br.com.cedi.model.Pessoa;
import br.com.cedi.service.AtividadeService;
import br.com.cedi.service.CertificadoService;
import br.com.cedi.service.HistoricoService;
import br.com.cedi.service.LoteCertificadoService;
import br.com.cedi.service.PessoaService;
import br.com.cedi.util.utilitarios.ChamarRelatorio;
import br.com.cedi.util.utilitarios.ExibirMensagem;
import br.com.cedi.util.utilitarios.GeradorCodigosProvisorios;
import br.com.cedi.util.utilitarios.RetornaLogado;
import br.com.cedi.util.utilitarios.ValidaCPF;

@ViewScoped
@Named
public class InicialMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer quantidadeCertificados = 0;
	private Integer quantidadeVisitas = 0;
	
	
	@Inject
	private CertificadoDAO certificadoDAO;
	
	@PostConstruct
	public void inicializar() {
	
		quantidadeCertificados = certificadoDAO.contarCertificados();
		
		
		quantidadeVisitas = certificadoDAO.contarImpressoes();	
	}
	
	public void testeEmail() {
		
		
	}
	

	public Integer getQuantidadeCertificados() {
		return quantidadeCertificados;
	}

	public void setQuantidadeCertificados(Integer quantidadeCertificados) {
		this.quantidadeCertificados = quantidadeCertificados;
	}

	public Integer getQuantidadeVisitas() {
		return quantidadeVisitas;
	}

	public void setQuantidadeVisitas(Integer quantidadeVisitas) {
		this.quantidadeVisitas = quantidadeVisitas;
	}
	
	
}