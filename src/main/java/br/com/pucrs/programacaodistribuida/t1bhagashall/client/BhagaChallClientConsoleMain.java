package br.com.pucrs.programacaodistribuida.t1bhagashall.client;


public class BhagaChallClientConsoleMain {

	public static void main (String[] args) {
		BhagaChallClientConsole console = new BhagaChallClientConsole();
		try{
			// Connecta no servidor
			console.conecta();
			// Registra usuário
			console.registra();
			// Verifica se entrou na partida
			while(console.aguardaPartida()){
				console.obtemOponente();
				
				while(console.ehMinhaVez()){
					console.obtemGrade();
					if(console.fazJogada()){
						console.obtemGrade();
					}else{
						break;
					}
				}
				//console.aguardarLiberacaoParaProximaPartida();
				//if(console.desejaContinuarJogando()){
					//continue;
				//}else{
					//break;
				//}
				
			}
			System.out.println("Bye!");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	

}
