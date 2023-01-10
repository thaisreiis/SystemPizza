package model.dao;

import DB.DB;
import model.dao.impl.ClientesDaoJDBC;

public class DaoFactory {

	/*Classe responsável por instanciar os Dao's. Isso é, terá operações estáticas para 
	instanciar os Daos. */
	
	public static ClientesDao createClienteDao() {
		return new ClientesDaoJDBC(DB.getConnection());
	}
	
}
