package model.dao;

import DB.DB;
import model.dao.impl.ClientesDaoJDBC;

public class DaoFactory {

	/*Classe respons�vel por instanciar os Dao's. Isso �, ter� opera��es est�ticas para 
	instanciar os Daos. */
	
	public static ClientesDao createClienteDao() {
		return new ClientesDaoJDBC(DB.getConnection());
	}
	
}
