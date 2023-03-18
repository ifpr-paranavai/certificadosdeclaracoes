package br.com.cedi.dao.hibernate;

import java.io.Serializable;
import java.util.List;


import br.com.cedi.dao.AtividadeDAO;
import br.com.cedi.dao.LoteCertificadoDAO;
import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Atividade;
import br.com.cedi.model.LoteCertificados;
import br.com.cedi.model.Pessoa;


public class HibernateAtividadeDAO extends HibernateGenericDAO<Atividade, Long> implements AtividadeDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
//	public List<ValorTotalVendaDoDia> buscarValorTotalVendaDoDia() {
//		return getEntityManager().createQuery("select "
//		          + "NEW com.algaworks.pedidovenda.dao.vo.ValorTotalVendaDoDia(p.dataVenda, sum(p.valorTotal)) "
//		          + "from Pedido p "
//		          + "group by p.dataVenda", ValorTotalVendaDoDia.class)
//		        .getResultList();
//	}
}
