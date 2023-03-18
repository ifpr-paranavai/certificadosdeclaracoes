package br.com.cedi.dao;

import java.io.Serializable;
import java.util.List;


public interface GenericDAO<T, ID extends Serializable> {
	
	public T buscarPeloCodigo(ID id);
	public void inserir(T entidade);
	public void atualizar(T entidade);
	public List<T> filtrar(T entidade, String... propriedades);
	public List<T> listarCondicao(String condicao);
	public List<T> listaComStatus();


}
