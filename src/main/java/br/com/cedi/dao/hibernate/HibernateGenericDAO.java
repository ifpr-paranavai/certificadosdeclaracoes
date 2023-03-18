package br.com.cedi.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.cedi.dao.GenericDAO;
import br.com.cedi.model.Pessoa;

public class HibernateGenericDAO<T, ID extends Serializable> implements GenericDAO<T, ID> {

	@Inject
	private EntityManager manager;
	
	private Class<T> classeEntidade;
	
	@SuppressWarnings("unchecked")
	public HibernateGenericDAO() {
		this.classeEntidade = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
		        .getActualTypeArguments()[0];
	}
	 
	@Override
	public T buscarPeloCodigo(ID id) {
		return manager.find(classeEntidade, id);
	}

	@Override
	public void atualizar(T entidade) {
		manager.merge(entidade);
	}
	
	@Override
	public void inserir(T entidade) {
		manager.persist(entidade);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> filtrar(T entidade, String... propriedades) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(classeEntidade);
		
		if (propriedades != null) {
			for (String propriedade : propriedades) {
				try {
					Object valor = PropertyUtils.getProperty(entidade, propriedade);
					if (valor != null) {
						if (valor instanceof String) {
							criteria.add(Restrictions.ilike(propriedade, (String) valor, MatchMode.ANYWHERE));
						} else {
							criteria.add(Restrictions.eq(propriedade, valor));
						}
					}
				} catch (Exception e) {
					throw new RuntimeException("Propriedade não encontrada", e);
				}
			}
		}
		
		return criteria.list();
	}
	
	protected EntityManager getEntityManager() {
		return manager;
	}
	
	protected Session getSession() {
		return manager.unwrap(Session.class);
	}

	@Override
	public List<T> listarCondicao(String condicao) {
		// TODO Auto-generated method stub
		//System.out.println("Classe Entidade: "+classeEntidade.getSimpleName());
		return manager.createQuery("from " + classeEntidade.getSimpleName() + " where status is true and " + condicao).getResultList();
//		return lr;
	}

	@Override
	public List<T> listaComStatus() {
		// TODO Auto-generated method stub
		return manager.createQuery("from " + classeEntidade.getSimpleName() + " where status is true order by id desc").getResultList();
	}



}
