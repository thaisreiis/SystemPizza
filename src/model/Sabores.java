package model;

import java.io.Serializable;
import java.util.Objects;

public class Sabores implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Ingredientes idIngredientes;
	//private Tipos idTipos;
	private String nome;
	private String idIngrediente1;
	private String idIngrediente2;
	private String idIngrediente3;
	private String idIngrediente4;
	private String idIngrediente5;
	
	private Ingredientes ingredientes;
	
	public Sabores() {}


	public Sabores(Integer id, String nome, String idIngrediente1, String idIngrediente2, String idIngrediente3,
			String idIngrediente4, String idIngrediente5) {
		super();
		this.id = id;
		this.nome = nome;
		this.idIngrediente1 = idIngrediente1;
		this.idIngrediente2 = idIngrediente2;
		this.idIngrediente3 = idIngrediente3;
		this.idIngrediente4 = idIngrediente4;
		this.idIngrediente5 = idIngrediente5;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Ingredientes getIdIngredientes() {
		return idIngredientes;
	}


	public void setIdIngredientes(Ingredientes idIngredientes) {
		this.idIngredientes = idIngredientes;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getIdIngrediente1() {
		return idIngrediente1;
	}


	public void setIdIngrediente1(String idIngrediente1) {
		this.idIngrediente1 = idIngrediente1;
	}


	public String getIdIngrediente2() {
		return idIngrediente2;
	}


	public void setIdIngrediente2(String idIngrediente2) {
		this.idIngrediente2 = idIngrediente2;
	}


	public String getIdIngrediente3() {
		return idIngrediente3;
	}


	public void setIdIngrediente3(String idIngrediente3) {
		this.idIngrediente3 = idIngrediente3;
	}


	public String getIdIngrediente4() {
		return idIngrediente4;
	}


	public void setIdIngrediente4(String idIngrediente4) {
		this.idIngrediente4 = idIngrediente4;
	}


	public String getIdIngrediente5() {
		return idIngrediente5;
	}


	public void setIdIngrediente5(String idIngrediente5) {
		this.idIngrediente5 = idIngrediente5;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id, idIngrediente1, idIngrediente2, idIngrediente3, idIngrediente4, idIngrediente5,
				idIngredientes, nome);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sabores other = (Sabores) obj;
		return Objects.equals(id, other.id) && Objects.equals(idIngrediente1, other.idIngrediente1)
				&& Objects.equals(idIngrediente2, other.idIngrediente2)
				&& Objects.equals(idIngrediente3, other.idIngrediente3)
				&& Objects.equals(idIngrediente4, other.idIngrediente4)
				&& Objects.equals(idIngrediente5, other.idIngrediente5)
				&& Objects.equals(idIngredientes, other.idIngredientes) && Objects.equals(nome, other.nome);
	}


	public Ingredientes getIngredientes() {
		return ingredientes;
	}
	
	public void setIngredientes(Ingredientes ingredientes) {
		this.ingredientes = ingredientes;		
	}


	
	
	

}
