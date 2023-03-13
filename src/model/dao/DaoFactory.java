package model.dao;

import DB.DB;
import model.dao.impl.ClientesDaoJDBC;
import model.dao.impl.IngredientesDaoJDBC;
import model.dao.impl.SaboresDaoJDBC;

public class DaoFactory {

	/*Classe respons�vel por instanciar os Dao's. Isso �, ter� opera��es est�ticas para 
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
