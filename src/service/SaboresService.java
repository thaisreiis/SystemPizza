package service;

import java.util.List;

import model.Ingredientes;
import model.Sabores;
import model.dao.DaoFactory;
import model.dao.IngredientesDao;
import model.dao.SaboresDao;

public class SaboresService {
	
private SaboresDao dao = DaoFactory.createSaboressDao();
	
	
	public List<Sabores> findAll() {
		return dao.findAll();
	}
	
	
	public void saveOrUpdate(Sabores obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}	
	
	public void remove(Sabores obj) {
		dao.deleteyId(obj.getId());
	}


}
