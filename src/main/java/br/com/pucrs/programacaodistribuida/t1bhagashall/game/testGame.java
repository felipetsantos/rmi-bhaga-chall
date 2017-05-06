package br.com.pucrs.programacaodistribuida.t1bhagashall.game;

public class testGame {
	public static void main (String[] args) {
		BhagaChallGame game = new BhagaChallGame();
		
		int id = game.registraJogador("Felipe 1");
		int id2 = game.registraJogador("Felipe2");

		int tem = game.temPartida(id);
		int tem2 = game.temPartida(id2);
		System.out.println("Tem:"+tem);
		System.out.println("Tem2:"+tem2);
		
		System.out.println("Obtem oponente id "+id+":"+game.obtemOponente(id));
		System.out.println("Obtem oponente id2 "+id2+":"+game.obtemOponente(id2));
		System.out.println("Tem2:"+tem2);
		
		System.out.println("Encerra id2:"+game.encerraPartida(id2));
		tem = game.temPartida(id);
		tem2 = game.temPartida(id2); 
		System.out.println("Tem:"+tem);
		System.out.println("Tem2:"+tem2);
	}
	

}
