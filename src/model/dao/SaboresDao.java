package model.dao;

import java.util.List;

import model.Sabores;

public interface SaboresDao {

	void insert(Sabores obj);
	void update(Sabores obj);
	void deleteyId(Integer id); 
	Sabores findById(Integer id); 
	List<Sabores> findAll(); 
	
	
	
}
