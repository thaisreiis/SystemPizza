package service;

import java.util.List;

import model.Ingredientes;
import model.dao.DaoFactory;
import model.dao.IngredientesDao;

public class IngredientesService {
	
	private IngredientesDao dao = DaoFactory.createIngredientesDao();
	
	
	public List<Ingredientes> findAll() {
		return dao.findAll();
	}
	
	
	public void saveOrUpdate(Ingredientes obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}	
	
	public void remove(Ingredientes obj) {
		dao.deleteyId(obj.getId());
	}

}
