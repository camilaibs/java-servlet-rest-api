package br.com.geladaonline.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Estoque {

	private Collection<Cerveja> cervejas = new ArrayList<>();
	
	public Estoque() {
		Cerveja primeira = new Cerveja("Stella Artois", "A cerveja belga mais francesa do mundo :)", "Artois", Cerveja.Tipo.LAGER);
		Cerveja segunda = new Cerveja("Erdinger Weissbier", "Cerveja de trigo alemã", "Erdinger Weissbräu", Cerveja.Tipo.WEIZEN);
		cervejas.add(primeira);
		cervejas.add(segunda);
	}
	
	public List<Cerveja> listarCervejas() {
		return new ArrayList<>(this.cervejas);
	}
	
	public void adicionarCervejas(Cerveja cerveja) {
		this.cervejas.add(cerveja);
	}
}
