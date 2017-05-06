package br.com.pucrs.programacaodistribuida.t1bhagashall.game;

import java.util.Map;
import java.util.TimerTask;

import br.com.pucrs.programacaodistribuida.t1bhagashall.models.Partida;

public class VerificacoesDoGame extends TimerTask{
	private BhagaChallGame game;
	
	public VerificacoesDoGame(BhagaChallGame game) {
		// TODO Auto-generated constructor stub
		this.game = game;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.verificaPartidasEncerradas();
	}
	
	private void verificaPartidasEncerradas(){
		
		Map<Integer,Partida> partidas =  this.game.getPartidasEncerradas();
		for(Partida p:partidas.values()){
			if(p.deveSerApagada()){
				game.removerPartidaEncerrada(p);
			}
		}
	}

}
