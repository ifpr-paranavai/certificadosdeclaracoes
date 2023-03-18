package br.com.cedi.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.cedi.dao.CertificadoDAO;
import br.com.cedi.model.Certificado;


public class HibernateCertificadoDAO extends HibernateGenericDAO<Certificado, Long> implements CertificadoDAO, Serializable {

	private static final long serialVersionUID = 1L;

	
@Override
	public Integer contarCertificados() {
		
		return Integer.parseInt(String.valueOf(getEntityManager().createNativeQuery("select count(id) from certificado where status is true").getSingleResult()));
	}
@Override
public Integer contarImpressoes() {
		
		return Integer.parseInt(String.valueOf(getEntityManager().createNativeQuery("select count(id) from dadosPesquisa where status is true").getSingleResult()));
	}

}
