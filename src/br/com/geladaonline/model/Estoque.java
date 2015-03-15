package br.com.geladaonline.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Estoque {

	private Map<String, Cerveja> cervejas = new HashMap<>();
	
	public Estoque() {
		Cerveja primeira = new Cerveja("Stella Artois", "A cerveja belga mais francesa do mundo :)", "Artois", Cerveja.Tipo.LAGER);
		Cerveja segunda = new Cerveja("Erdinger Weissbier", "Cerveja de trigo alemã", "Erdinger Weissbräu", Cerveja.Tipo.WEIZEN);
		adicionarCervejas(primeira);
		adicionarCervejas(segunda);
	}
	
	public List<Cerveja> listarCervejas() {
		return new ArrayList<>(this.cervejas.values());
	}
	
	public void adicionarCervejas(Cerveja cerveja) {
		this.cervejas.put(cerveja.getNome(), cerveja);
	}
	
	public Cerveja recuperarCervejaPeloNome(String nome) {
		return cervejas.get(nome);
	}
	
}
