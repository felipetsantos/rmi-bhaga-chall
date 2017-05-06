package br.com.pucrs.programacaodistribuida.t1bhagashall.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;

import br.com.pucrs.programacaodistribuida.t1bhagashall.libs.BhagaChallInterface;

public class BhagaChallClientConsole {
	private BhagaChallInterface client;
	private boolean ehCabra;
	private int jogadorId;
	private BufferedReader br;
	private int jogadas;
	
	public BhagaChallClientConsole() {
		this.br = new BufferedReader(new InputStreamReader(System.in));
		this.jogadas =0;
	}
	public void conecta() throws IOException{
		System.out.println("Digite o ip do servidor:");
		String ip = this.br.readLine();
		try {
			this.client = (BhagaChallInterface) Naming.lookup ("//"+ip+"/BhagaChall");
			System.out.println("Conectado com sucesso!:");
		} catch (Exception e) {
			System.out.println ("BhagaChall Client failed.");
			e.printStackTrace();
		}
	}
	
	public void registra() throws IOException{
		boolean success = true;
		do{
			System.out.println("Digite o nome do jogador:");
			String nome = this.br.readLine();
			this.jogadorId = this.client.registraJogador(nome);
			if(this.jogadorId == -1){
				System.out.println("jah existe um usuario como nome: "+nome+".");
			}else if(this.jogadorId == -2){
				System.out.println("O numero maximo de usuarios ja foi atingido. Tente novamente mais tarde.");
				success = false;
			}
		}while(this.jogadorId < 0 && success);
		if(success){
			System.out.println("Registro efetuado com sucesso! Id:"+this.jogadorId);
		}
	}
	
	public boolean aguardaPartida() throws RemoteException, InterruptedException {
		boolean entrouNaPartida = false;
		boolean success = false;
		System.out.println("Aguardando oponente...");
		while (!entrouNaPartida) {
			
			int temPartida = this.client.temPartida(this.jogadorId);
			
			switch (temPartida) {
				case -2:
					System.out.println("Tempo de espera esgotado");
					success = false;
					entrouNaPartida = true;
					break;
				case -1:
					System.out.println("Erro");
					success = false;
					entrouNaPartida = true;
					break;
				case 0:
					success = true;
					entrouNaPartida = false;
					break;
				case 1:
					this.ehCabra = true;
					success = true;
					entrouNaPartida = true;
					System.out.println("A partida vai comecar. Voce e a cabra.");
					break;
				case 2:
					success = true;
					entrouNaPartida = true;
					this.ehCabra = false;
					System.out.println("A partida vai comecar. Voce eh o trigre.");
					break;
				default:
					break;
			}
			if(entrouNaPartida){
				break;
			}
			Thread.sleep(1000);
		}
		return success;
	}
	
	public boolean ehMinhaVez() throws RemoteException, InterruptedException{
		boolean esperarAVez = true;
		boolean continuar = false;
		if(!this.ehCabra || this.jogadas > 0){
			System.out.println("Aguardando o oponente jogar...");
		}
		while(esperarAVez){
			switch (this.client.ehMinhaVez(this.jogadorId)) {
				case -2:
					// Ainda n�o ha 2 jogadores registrados na partida.
					System.out.println("Ainda nao ha 2 jogadores registrados na partida");
					continuar = esperarAVez = false;
					break;
				case -1:
					// ERRO
					System.out.println("Erro");
					continuar = esperarAVez = false;
					break;
				case  0:
					// N�o � sua vez
					esperarAVez = true;
					break;
				case  1:
					System.out.println("Prepare para sua jogada.");
					esperarAVez = false;
					continuar = true;
					break;
				case  2:
					esperarAVez = false;
					continuar = false;
					System.out.println("Voce ganhou a partida.");
					break;
				case  3:
					esperarAVez = false;
					continuar = false;
					System.out.println("Voce perdeu a partida.");
					break;
				case  4:
					esperarAVez = false;
					continuar = false;
					System.out.println("A partida terminou empatada.");
					break;
				case  5:
					esperarAVez = false;
					continuar = false;
					System.out.println("Voce ganhou a partida por WO.");
					break;
				case  6:
					esperarAVez = false;
					continuar = false;
					System.out.println("Voce perdeu partida por WO.");
					break;

				default:
					break;
			}
			Thread.sleep(1000);
		}
		return continuar;
	}
	
	public boolean desejaContinuarJogando() throws IOException{
		System.out.println("Desja continuar jogando?(s/n):");
		String resposta = this.br.readLine();
		if(resposta.toLowerCase().equals("n")){
			return false;
		}else{
			return true;
		}
	}
	
	public void obtemOponente() throws RemoteException{
		
		String oponente = this.client.obtemOponente(this.jogadorId);
		if(oponente != null){
			System.out.println("O seu oponente eh:"+oponente);
		}else{
			System.out.println("Não foi possível localizar o oponente.");
		}
	}
	
	public void obtemGrade() throws RemoteException{
		String grade = this.client.obtemGrade(this.jogadorId);
		int i= 0;
		System.out.println("grade formato plain");
		System.out.println(grade);
		System.out.println("grade formatada:");
		for(char ch: grade.toCharArray()){
			System.out.print(ch);
			i++;
			if(i== 5){
				System.out.print("\n");
				i=0;
			}
		}
		
	}
	
	public boolean fazJogada() throws IOException{
		if(this.ehCabra){
			if(this.jogadas < 20){
				return this.posicionaCabra();
			}else{
				return this.moveCabra();
			}
		//System.out.println("Solicita jogada");
		}else{
			return this.moveTigre();
		}
	}
	
	public boolean posicionaCabra() throws IOException{
		boolean continuaAPartida = true;
		boolean lerNovamente  = false;
		do{
			Integer x,y;
			x = null;
			y = null;
			do{
				System.out.println("Digite posicao Y do tabuleiro onde cabra deve posicionada(Variando de 0 a 4,inclusive):");
				String strY = br.readLine();
				try{
					y = Integer.parseInt(strY);
				}catch (NumberFormatException e) {
					// TODO: handle exception
					System.out.println(strY+" nao eh um numero inteoiro.");
				}
			}while(y == null);
			do{
				System.out.println("Digite posicao X do tabuleiro onde cabra deve posicionada(Variando de 0 a 4,inclusive)::");
				String strX = br.readLine();
				try{
					x = Integer.parseInt(strX);
				}catch (NumberFormatException e) {
					// TODO: handle exception
					System.out.println(strX+" nao eh um numero inteoiro.");
				}
			}while(x == null);
			

			if(x != null && y != null){
				int result = this.client.posicionaCabra(this.jogadorId, x, y);
				switch (result) {
				case 2:
					// Partida encerrada
					continuaAPartida = false;
					lerNovamente = false;
					System.out.println("Partida encerrada, tempo de espera por jogada jah tinha esgotado.");
					break;
				case 1:
					//tudo certo
					this.jogadas++;
					continuaAPartida = true;
					lerNovamente = false;
					System.out.println("Jogada efetuada com sucesso");
					break;
				case 0:
					//Movimento invalido
					continuaAPartida = true;
					lerNovamente =true;
					System.out.println("O movimento executado eh invalido");
					break;
				case -1:
					lerNovamente = false;
					continuaAPartida = false;
					System.out.println("O ocorreu um erro desconhecido");
					//Erro desconhecido	
					break;
				case -2:
					//Partida sem jogadores
					lerNovamente = false;
					continuaAPartida = false;
					System.out.println("Voce ainda nao tem oponente");
					break;
				case -3:
					// Ainda nao eh sua vez
					lerNovamente = false;
					continuaAPartida = true;
					System.out.println("Ainda nao eh sua vez.");
					break;
				case -4:	
					// Animal incorreto
					lerNovamente = false;
					continuaAPartida = true;
					System.out.println("Animal incorreto.");
					this.ehCabra = this.ehCabra ? false:true;
					break;
				case -5:
					//Todas as cabras jah foram posicionadas
					lerNovamente = false;
					continuaAPartida = true;
					System.out.println("Todas as cabras jah foram posicionadas.");
					break;
				default:
					break;
				}
			}
		}while(lerNovamente);
		
		return continuaAPartida;
		
	}
	
	public boolean moveCabra() throws IOException{
		boolean continuaAPartida = true;
		boolean lerNovamente  = false;
		do{
			Integer cabra,direcao;
			cabra = null;
			direcao = null;
			do{
				System.out.println("Digite o a da cabra que deseja mover:(variando de 0 a 19:inclusive):");
				String strTigre = br.readLine();
				try{
					cabra = Integer.parseInt(strTigre);
				}catch (NumberFormatException e) {
					// TODO: handle exception
					System.out.println("O numero do tigre deve ser um numero de 0 a 3, inclusive.");
				}
			}while(cabra == null);
			
			do{
				System.out.println("Digite a direcao(0=direita,1=direita/abaixo,2=baixo,3=esquerda/abaixo,4=esquerda,5=esquerda/acima,6=cima,7=direta/acima):");
				String strDirecao = br.readLine();
				try{
					direcao = Integer.parseInt(strDirecao);
				}catch (NumberFormatException e) {
					// TODO: handle exception
					System.out.println("O numero da diracao deve ser um numero de 0 a 7, inclusive.");
				}
			}while(direcao == null);
			if(direcao != null && cabra != null){
				int result = this.client.moveCabra(this.jogadorId, cabra, direcao);
				switch (result) {
				case 2:
					// Partida encerrada
					continuaAPartida = false;
					lerNovamente = false;
					System.out.println("Partida encerrada, tempo de espera por jogada jah tinha esgotado.");
					break;
				case 1:
					//tudo certo
					this.jogadas++;
					continuaAPartida = true;
					lerNovamente = false;
					System.out.println("Jogada efetuada com sucesso");
					break;
				case 0:
					//Movimento inv�lido
					continuaAPartida = true;
					lerNovamente =true;
					System.out.println("O movimento executado eh invalido");
					break;
				case -1:
					lerNovamente = false;
					continuaAPartida = false;
					System.out.println("O ocorreu um erro desconhecido");
					//Erro desconhecido	
					break;
				case -2:
					//Partida sem jogadores
					lerNovamente = false;
					continuaAPartida = false;
					System.out.println("Voce ainda nao tem oponente");
					break;
				case -3:
					// Ainda nao eh sua vez
					lerNovamente = false;
					continuaAPartida = true;
					System.out.println("Ainda nao eh sua vez.");
					break;
				case -4:	
					// Animal incorreto
					lerNovamente = false;
					continuaAPartida = true;
					System.out.println("Animal incorreto.");
					this.ehCabra = this.ehCabra ? false:true;
					break;
				case -5:
					//Direrecao invalida
					lerNovamente = false;
					continuaAPartida = true;
					System.out.println("Direcao invalida.");
					break;
				default:
					break;
				}
			}		
		}while(lerNovamente);
		
		return continuaAPartida;
	}
	
	public boolean moveTigre() throws IOException{
		boolean continuaAPartida = true;
		boolean lerNovamente  = false;
		do{
			Integer tigre,direcao;
			tigre = null;
			direcao = null;
			do{
				System.out.println("Digite o numero do tigre que deseja mover:(variadno de de 0 a 3,inclusive):");
				String strTigre = br.readLine();
				try{
					tigre = Integer.parseInt(strTigre);
				}catch (NumberFormatException e) {
					// TODO: handle exception
					System.out.println("O numero do tigre deve ser um numero de 0 a 3, inclusive.");
				}
			}while(tigre == null);
			
			do{
				System.out.println("Digite a direcao(0=direita,1=direita/abaixo,2=baixo,3=esquerda/abaixo,4=esquerda,5=esquerda/acima,6=cima,7=direta/acima):");
				String strDirecao = br.readLine();
				try{
					direcao = Integer.parseInt(strDirecao);
				}catch (NumberFormatException e) {
					// TODO: handle exception
					System.out.println("O numero da direcao deve ser um numero de 0 a 7, inclusive.");
				}
			}while(direcao == null);
			if(direcao != null && tigre != null){
				int result = this.client.moveTigre(this.jogadorId, tigre, direcao);
				switch (result) {
				case 2:
					// Partida encerrada
					continuaAPartida = false;
					lerNovamente = false;
					System.out.println("Partida encerrada, tempo de espera por jogada jah tinha esgotado.");
					break;
				case 1:
					//tudo certo
					this.jogadas++;
					continuaAPartida = true;
					lerNovamente = false;
					System.out.println("Jogada efetuada com sucesso");
					break;
				case 0:
					//Movimento invalido
					continuaAPartida = true;
					lerNovamente =true;
					System.out.println("O movimento executado eh invalido");
					break;
				case -1:
					lerNovamente = false;
					continuaAPartida = false;
					System.out.println("O ocorreu um erro desconhecido");
					//Erro desconhecido	
					break;
				case -2:
					//Partida sem jogadores
					lerNovamente = false;
					continuaAPartida = false;
					System.out.println("Voce ainda nao tem oponente");
					break;
				case -3:
					// Ainda  nao eh sua vez
					lerNovamente = false;
					continuaAPartida = true;
					System.out.println("Ainda nao e sua vez.");
					break;
				case -4:	
					// Animal incorreto
					lerNovamente = false;
					continuaAPartida = true;
					System.out.println("Animal incorreto.");
					break;
				case -5:
					//Direcao invalida
					lerNovamente = false;
					continuaAPartida = true;
					System.out.println("Direcao invalida.");
					break;
				default:
					break;
				}
			}		
		}while(lerNovamente);
		
		return continuaAPartida;
	}
	
	public void aguardarLiberacaoParaProximaPartida() throws InterruptedException{
		System.out.println("Voce vai ter que aguardar 60 segundos para come�ar uma nova partida...");
		Thread.sleep(60000);
	}
}
