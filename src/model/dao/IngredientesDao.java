package model.dao;

import java.util.List;

import model.Ingredientes;

public interface IngredientesDao {

	void insert(Ingredientes obj);
	void update(Ingredientes obj);
	void deleteyId(Integer id); 
	List<Ingredientes> findAll(); 
	
	

}
