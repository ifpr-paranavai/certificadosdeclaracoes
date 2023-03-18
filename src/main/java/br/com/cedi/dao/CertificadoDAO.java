package br.com.cedi.dao;


import br.com.cedi.model.Certificado;


public interface CertificadoDAO extends GenericDAO<Certificado, Long> {

//	public List<ValorTotalVendaDoDia> buscarValorTotalVendaDoDia();
	public Integer contarCertificados();
	
public Integer contarImpressoes();
}
