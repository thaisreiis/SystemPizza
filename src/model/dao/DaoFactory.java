package model.dao;

import DB.DB;
import model.dao.impl.ClientesDaoJDBC;
import model.dao.impl.IngredientesDaoJDBC;
import model.dao.impl.SaboresDaoJDBC;

public class DaoFactory {

	/*Classe responsável por instanciar os Dao's. Isso é, terá operações estáticas para 
	instanciar os Daos. */
	
	public static ClientesDao createClienteDao() {
		return new ClientesDaoJDBC(DB.getConnection());
	}
	
	public static IngredientesDao createIngredientesDao() {
		return new IngredientesDaoJDBC(DB.getConnection());
	}	
	
	public static SaboresDao createSaboressDao() {
		return new SaboresDaoJDBC(DB.getConnection());
	}	
}
