package br.com.cedi.util.utilitarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.internal.EntityManagerImpl;

import br.com.cedi.dao.AtividadeDAO;
import br.com.cedi.dao.GenericDAO;
import br.com.cedi.dao.hibernate.HibernateGenericDAO;
import br.com.cedi.model.Atividade;


public class Conexao{

	@Inject
	private EntityManager manager;
//	private EntityManager manager = Persistence.createEntityManagerFactory("certificadosDeclaracoesPU").createEntityManager();
	
	
	
    public Connection getConnections(){
        EntityManagerImpl factory = (EntityManagerImpl) manager;
             SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) factory.getSession().getSessionFactory();
         try {
        	             return sessionFactoryImpl.getConnectionProvider().getConnection();
         } catch (SQLException ex) {
             Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
         }
         return null;
     }
//    
//	protected EntityManager getEntityManager() {
//		return manager;
//	}
	
//	protected Session getSession() {
//		return manager.unwrap(Session.class);
//	}
     
}
