package model.dao;

import java.util.List;

import model.Clientes;

public interface ClientesDao {

	//CRUD
	
	void insert(Clientes obj); //inserir no banco de dados
	void update(Clientes obj);
	void deleteyId(Clientes obj); 
	Clientes findById(Integer id); //consultar no banco de dados
	List<Clientes> findAll(); // retornar todos os clientes
	
}
