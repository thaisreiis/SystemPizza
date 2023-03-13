package model;

import java.io.Serializable;
import java.util.Objects;

public class Ingredientes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nomeIngrediente;
	
	public Ingredientes() {}

	public Ingredientes(Integer id, String nomeIngrediente) {
		this.id = id;
		this.nomeIngrediente = nomeIngrediente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeIngrediente() {
		return nomeIngrediente;
	}

	public void setNomeIngrediente(String nomeIngrediente) {
		this.nomeIngrediente = nomeIngrediente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nomeIngrediente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingredientes other = (Ingredientes) obj;
		return Objects.equals(id, other.id) && Objects.equals(nomeIngrediente, other.nomeIngrediente);
	}

	@Override
	public String toString() {
		return "Ingredientes [id=" + id + ", nomeIngrediente=" + nomeIngrediente + "]";
	}	
}
