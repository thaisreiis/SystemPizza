package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DB.DB;
import DB.DbException;
import DB.DbIntegrityException;
import model.Ingredientes;
import model.dao.IngredientesDao;

public class IngredientesDaoJDBC implements IngredientesDao {
	
	private Connection conn;

	public IngredientesDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Ingredientes obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO ingredientes " 
					 + "(Nome) " 
					 + "VALUES " 
					 + "(?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNomeIngrediente());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Ingredientes obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE ingredientes " + "SET Nome = ?" + "WHERE Id = ?");

			st.setString(1, obj.getNomeIngrediente());
			st.setInt(2, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}	
	}

	@Override
	public void deleteyId(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM ingredientes WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}


	@Override
	public List<Ingredientes> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM ingredientes ORDER BY Nome");
			rs = st.executeQuery();

			List<Ingredientes> list = new ArrayList<>();

			while (rs.next()) {
				Ingredientes obj = new Ingredientes();
				obj.setId(rs.getInt("Id"));
				obj.setNomeIngrediente(rs.getString("Nome"));
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
