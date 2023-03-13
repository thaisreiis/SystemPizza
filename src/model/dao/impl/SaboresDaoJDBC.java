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
import model.Sabores;
import model.dao.SaboresDao;

public class SaboresDaoJDBC implements SaboresDao{
	
	private Connection conn;

	public SaboresDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Sabores obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO sabores " 
					+ "(id_saborIngredientes, id_tipos, nome, id_ingrediente1, id_ingrediente2, id_ingrediente3, id_ingrediente4, id_ingrediente5) " 
					+ "VALUES " 
					+ "(?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, 1);
			st.setInt(2, 1);
			st.setString(3, obj.getNome());
			st.setString(4, obj.getIdIngrediente1());
			st.setString(5, obj.getIdIngrediente2());
			st.setString(6, obj.getIdIngrediente3());
			st.setString(7, obj.getIdIngrediente4());
			st.setString(8, obj.getIdIngrediente5());
			
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
	public void update(Sabores obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE sabores " 
			         + "SET Nome = ?, id_ingrediente1 = ?, id_ingrediente2 = ?, id_ingrediente3 = ?, id_ingrediente4 = ?, id_ingrediente5 = ? " 
					 + "WHERE Id = ?"
			         );

			st.setString(1, obj.getNome());		
			st.setString(2, obj.getIdIngrediente1());
			st.setString(3, obj.getIdIngrediente2());
			st.setString(4, obj.getIdIngrediente3());
			st.setString(5, obj.getIdIngrediente4());
			st.setString(6, obj.getIdIngrediente5());
			st.setInt(7, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM sabores WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Sabores findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Sabores instantiateSabores(ResultSet rs, Ingredientes dep) throws SQLException {
		Sabores obj = new Sabores();
		obj.setId(rs.getInt("Id"));
		obj.setNome(rs.getString("Name"));
		obj.setIdIngrediente1(rs.getString("idIngrediente1"));
		obj.setIdIngrediente2(rs.getString("idIngrediente2"));
		obj.setIdIngrediente2(rs.getString("idIngrediente3"));
		obj.setIdIngrediente2(rs.getString("idIngrediente4"));
		obj.setIdIngrediente2(rs.getString("idIngrediente5"));
		obj.setIngredientes(dep);
		return obj;
	}

	private Ingredientes instantiateIngredientes(ResultSet rs) throws SQLException {
		Ingredientes dep = new Ingredientes();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setNomeIngrediente(rs.getString("DepNome"));
		return dep;
	}

	@Override
	public List<Sabores> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM sabores ORDER BY Nome");
			rs = st.executeQuery();

			List<Sabores> list = new ArrayList<>();

			while (rs.next()) {
				Sabores obj = new Sabores();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome"));
				obj.setIdIngrediente1(rs.getString("id_ingrediente1"));
				obj.setIdIngrediente2(rs.getString("id_Ingrediente2"));
				obj.setIdIngrediente3(rs.getString("id_ingrediente3"));
				obj.setIdIngrediente4(rs.getString("id_ingrediente4"));
				obj.setIdIngrediente5(rs.getString("id_ingrediente5"));
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
