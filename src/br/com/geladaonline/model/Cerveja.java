package br.com.geladaonline.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Cerveja {
	
	public enum Tipo {
		LAGER, PILSEN, PALE_ALE, INDIAN_PALE_ALE, WEIZEN;
	}
	
	private String nome;
	private String descricao;
	private String cerveja;
	private Tipo tipo;
	
	public Cerveja() {}
	
	public Cerveja(String nome, String descricao, String cerveja, Tipo tipo) {
		this.nome = nome;
		this.descricao = descricao;
		this.cerveja = cerveja;
		this.tipo = tipo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return this.nome + " - " + this.descricao;
	}
	
}
