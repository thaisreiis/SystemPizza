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
import model.Clientes;
import model.dao.ClientesDao;

public class ClientesDaoJDBC implements ClientesDao {

	// Os dois codigos abaixo é para criar a conexao com o banco de dados só
	// chamando o CONN:

	private Connection conn;

	public ClientesDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Clientes obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO clientes " + "(Nome, Sobrenome, Telefone) " + "VALUES " + "(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setString(2, obj.getSobrenome());
			st.setString(3, obj.getTelefone());

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
	public void update(Clientes obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE clientes " + "SET Nome = ?, Sobrenome = ?, Telefone = ? " + "WHERE Id = ?");

			st.setString(1, obj.getNome());
			st.setString(2, obj.getSobrenome());
			st.setString(3, obj.getTelefone());
			st.setInt(4, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM clientes WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Clientes findById(Integer id) {
		return null;

	}

	@Override
	public List<Clientes> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM clientes ORDER BY Nome");
			rs = st.executeQuery();

			List<Clientes> list = new ArrayList<>();

			while (rs.next()) {
				Clientes obj = new Clientes();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome"));
				obj.setSobrenome(rs.getString("Sobrenome"));
				obj.setTelefone(rs.getString("Telefone"));
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
