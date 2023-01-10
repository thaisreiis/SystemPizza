package model;

import java.io.Serializable;
import java.util.Objects;

public class Clientes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private String sobrenome;
	private String telefone;
	
	public Clientes() {}

	public Clientes(Integer id, String nome, String sobrenome, String telefone) {
		this.id = id;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.telefone = telefone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, nome, sobrenome, telefone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clientes other = (Clientes) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& Objects.equals(sobrenome, other.sobrenome) && Objects.equals(telefone, other.telefone);
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", telefone=" + telefone + "]";
	}


	
	
}
