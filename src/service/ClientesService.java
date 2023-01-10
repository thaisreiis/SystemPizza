package service;

import java.util.ArrayList;
import java.util.List;

import model.Clientes;
import model.dao.ClientesDao;
import model.dao.DaoFactory;

public class ClientesService {

	private ClientesDao dao = DaoFactory.createClienteDao();
	
	
	public List<Clientes> findAll() {
		return dao.findAll();
	}
	
	
	public void saveOrUpdate(Clientes obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}	
}
