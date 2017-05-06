package br.com.pucrs.programacaodistribuida.t1bhagashall.models;

import java.util.HashMap;
import java.util.Map;

public class Casa {
	public int x;
	public int y;
	public Peca peca;
	
	public Map<Integer,Casa> movimentosSimplesPossiveis;
	public Map<Integer,Casa> pulosPossiveisParaTigres;
	
	public Casa(Peca peca){
		this.peca = peca;
		this.movimentosSimplesPossiveis = new HashMap<Integer, Casa>();
		this.pulosPossiveisParaTigres  = new HashMap<Integer, Casa>();
	}
}
