package br.com.pucrs.programacaodistribuida.t1bhagashall.game;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import br.com.pucrs.programacaodistribuida.t1bhagashall.models.Jogador;
import br.com.pucrs.programacaodistribuida.t1bhagashall.models.Partida;

public class BhagaChallGame {
	public static final BhagaChallGame controle = new BhagaChallGame();
	
	private Map<Integer,Jogador> jogadores;
	private Map<Integer,Jogador> jogadoresEsperando;
	private Map<Integer,Partida> partidas;
	private Map<Integer,Integer> indiceJogadorIdParaPartidaId;
	private Map<Integer,Partida> partidasEncerradas;
	private Timer timer;
	private Object lock = new Object();
	
	public BhagaChallGame(){
		this.jogadores = new HashMap<Integer,Jogador>();
		this.partidas = new HashMap<Integer,Partida>();
		this.partidasEncerradas = new HashMap<Integer,Partida>();
		this.indiceJogadorIdParaPartidaId =  new HashMap<Integer,Integer>();
		this.jogadoresEsperando = new  HashMap<Integer,Jogador>();
		
		timer = new Timer();
		timer.schedule(new VerificacoesDoGame(this),0, 1000);
	}
	
	////##### OPCOES DO JOGO
	
	// 1) Registra jogador
	public int registraJogador(String nome){
		// valida se não ultrapassou o tamanho
		if(this.jogadores.size() == 100){
			return -2;
		}
		
		// valida se o jogador já existe
		if(!this.existe(nome)){
			synchronized(lock) {
				Jogador j = this.adicionaJogador(nome);
				if(this.jogadoresEsperando.size() > 0){
					Jogador j1 = this.jogadoresEsperando.values().iterator().next();
					this.criarAdicionarPartida(j1,j);
				}else{
					this.jogadoresEsperando.put(j.id,j);
				}
				return j.id;
			}
		}else{
			return -1;
		}
	}
	
	// 2) Encerra a partida
	public int encerraPartida(int id){
		Integer partidaId = this.indiceJogadorIdParaPartidaId.get(id);
		if(partidaId != null){
			Partida partida = this.partidas.get(partidaId);
			if(partida != null){
				partida.encerraPartida(id,"Jogador solicitou o encerramento de partida");
				this.partidasEncerradas.put(partidaId,partida);
				//this.liberaJogador(partida.jogador1);
				//this.liberaJogador(partida.jogador2);
				return 0;
			}else{
				return -1;
			}
		}
		return -1;
	}
	
	// 3) Verifica se tem partida
	public int temPartida(int id){
		
		this.verificaSeAlgumJogadorLiberou();
		
		Jogador j = this.jogadores.get(id);
		if(j != null){
			if(j.estaOcupado){
				Partida p = this.partidas.get(this.indiceJogadorIdParaPartidaId.get(id));
				if(p.temVencedor()){
					// ainda não há uma nova partida
					return 0;
				// Testa se o jogador não ultrapassou o tempo de espera por partida
				}if(j.ehCabra){
					// tem partida e é cabra
					return 1;
				}else{
					// tem partida e é tigre
					return 2;
				}
			}else if(!j.aindaDeveEsperarPorPartida()){
				this.removeJogadorSemPartida(id);
				// tempo de espera esgotado
				return -2;
			}else{
				// ainda não há partida
				return 0;
			}
		}
		
		return -1;
	}
	
	// 4) Verifica se é a vez do jogador
	public int ehMinhaVez(int id){
		
		Integer partidaId = this.indiceJogadorIdParaPartidaId.get(id);
		
		if(partidaId != null){
			Partida partida = this.partidas.get(partidaId);
			if(partida != null){
				if(partida.temVencedor()){
					if(partida.vencedorId == id){
						// é o vencedor
						return 2;
					}else{
						// é o perdedor
						return 3;
					}
				}else if(partida.ehEmpate()){
					// Terminou em empate
					return 4;
				}else if(partida.aindaDeveEsperarPorJogada()){
				
					if(partida.quemFezAUltimaJogada == id || (partida.quemFezAUltimaJogada == -1 && !this.jogadores.get(id).ehCabra)){
						// Não é minha vez
						return 0;
					}else{
						// é minha vez
						return 1;
					}
				}else if(partida.ninguemJogou()){
					if(this.jogadores.get(id).ehCabra){
						// Perdedor por WO
						partida.encerraPartida(this.jogadores.get(id).id,"Jogador "+this.jogadores.get(id).nome+" demorou muito para jogar.");
						this.partidasEncerradas.put(partidaId,partida);
						return 6;
					}else{
						int idPerdedor = partida.jogador1.ehCabra ? partida.jogador1.id:partida.jogador2.id;
						partida.encerraPartida(idPerdedor,"Jogador "+this.jogadores.get(idPerdedor).nome+" demorou muito para jogar.");
						this.partidasEncerradas.put(partidaId,partida);
						// Vencedor por WO
						return 5;
					}

				}else if(partida.quemFezAUltimaJogada == id){
					// Perdedor por WO
					partida.encerraPartida(id,"Jogador "+this.jogadores.get(id).nome+" demorou muito para jogar.");
					this.partidasEncerradas.put(partidaId,partida);
					return 6;
				}else if(partida.quemFezAUltimaJogada != id){
					// Vencedor por WO
					int idPerdedor = partida.jogador1.id == id ? partida.jogador2.id:partida.jogador1.id;
					partida.encerraPartida(idPerdedor,"Jogador "+this.jogadores.get(idPerdedor).nome+" demorou muito para jogar.");
					this.partidasEncerradas.put(partidaId,partida);
					return 5;
				}
			}else{
				// ERRO
				return -1;
			}
		}else{
			return -2;
		}
		// ERRO
		return -1;
	}

	// 5) Retorna o tabuleiro
	public String obtemGrade(int id){
		Integer pId = this.indiceJogadorIdParaPartidaId.get(id);
		if(pId != null){
			Partida p = this.partidas.get(pId);
			return p.tabuleiro.toString();
		}else{
			return null;
		}
	}
	
	// 6) Move tigre
	public int moveTigre(int id,int tigreId,int codigoDirecao){
		Integer partidaId = this.indiceJogadorIdParaPartidaId.get(id);
		if(partidaId != null){
			Partida partida = this.partidas.get(partidaId);
			if(partida != null){
				if(partida.aindaDeveEsperarPorJogada()){
					Jogador j = this.jogadores.get(id);
					if(j != null){
						int retorno = partida.moveTigre(j,tigreId,codigoDirecao);
						if(partida.temVencedor()){
							this.partidasEncerradas.put(partidaId, partida);
						}
						return retorno;
					}else{
						// ERRO
						return -1;
					}
				}else{
					//Patida encerrada por timeout
					return 2;
				}
			}else{
				// ERRO
				return -1;
			}
		}else{
			// Não há dois jogadores registrados
			return -2;
		}
	}
	
	//7) Coloca a cabra na posição
	public int posicionaCabra(int id,int x,int y){
		Integer partidaId = this.indiceJogadorIdParaPartidaId.get(id);
		if(partidaId != null){
			Partida partida = this.partidas.get(partidaId);
			if(partida != null){
				if(partida.aindaDeveEsperarPorJogada()){
					Jogador j = this.jogadores.get(id);
					int retorno = partida.posicionaCabra(j,x,y);
					if(partida.temVencedor()){
						this.partidasEncerradas.put(partidaId, partida);
					}
					return retorno;
				}else{
					//Patida encerrada por timeout
					return 2;
				}
			}else{
				// ERRO
				return -1;
			}
		}else{
			// Não há dois jogadores registrados
			return -2;
		}
	}
	
	//8) Move a cabra
	public int moveCabra(int id,int cabraId,int codigoDirecao){
		Integer partidaId = this.indiceJogadorIdParaPartidaId.get(id);
		if(partidaId != null){
			Partida partida = this.partidas.get(partidaId);
			if(partida != null){
				if(partida.aindaDeveEsperarPorJogada()){
					Jogador j = this.jogadores.get(id);
					if(j != null){
						int retorno = partida.moveCabra(j,cabraId,codigoDirecao);
						if(partida.temVencedor()){
							this.partidasEncerradas.put(partidaId, partida);
						}
						return retorno;
					}else{
						// ERRO
						return -1;
					}
				}else{
					//Patida encerrada por timeout
					return 2;
				}
			}else{
				// ERRO
				return -1;
			}
		}else{
			// Não há dois jogadores registrados
			return -2;
		}		
	}
	
	// 9) Obtem o oponente
	public String obtemOponente(int id){
		int idPartida = this.indiceJogadorIdParaPartidaId.get(id);
		Partida p = this.partidas.get(idPartida);
		if(p != null){
			if(p.jogador1.id == id){
				return p.jogador2.nome;
			}else if(p.jogador2.id == id){
				return p.jogador1.nome;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	//##### GETS
	public Map<Integer,Jogador> getJogadoresEsperando(){
		return this.jogadoresEsperando;
	}
	public Map<Integer,Partida> getPartidasEncerradas(){
		return this.partidasEncerradas;
	}
	
	//##### MÉTODOS AUXILIARES
	//********************
	public void verificaSeAlgumJogadorLiberou(){

			if(this.jogadoresEsperando.size() > 1){
				int i =0;
				Jogador[] oponentes = new Jogador[2];
				for(Map.Entry<Integer, Jogador> a:this.jogadoresEsperando.entrySet()){
					oponentes[i] = a.getValue();
					i++;
					if(i== 2){
						break;
					}
				}
				Jogador j1 = oponentes[0];
				Jogador j2 = oponentes[1];
				this.criarAdicionarPartida(j1,j2);
				System.out.println("Iniciando partida entre "+j1.nome+"x"+j2.nome);
			}

	}
	public void removerPartidaEncerrada(Partida p){
		System.out.println("Removeu partida encerrada entre "+p.jogador1.nome+"x"+p.jogador2.nome);
		this.partidas.remove(p.id);
		this.partidasEncerradas.remove(p.id);
		this.liberaJogador(p.jogador1);
		this.liberaJogador(p.jogador2);
		this.indiceJogadorIdParaPartidaId.remove(p.jogador1.id);
		this.indiceJogadorIdParaPartidaId.remove(p.jogador2.id);
	}
	public  void removeJogadorSemPartida(int id){
		this.jogadores.remove(id);
		this.jogadoresEsperando.remove(id);
	}
	private int getRandomId(){
		Random r = new Random();
		int minimo = 1;
		int maximo = 100;
		int resultado = r.nextInt(maximo-minimo);
		return resultado;
	}
	
	private boolean existe(String nome){
		for(Jogador j: jogadores.values()){
			if(j.nome.equals(nome)){
				return true;
			}
		}
		return false;
	}
	
	
	private Jogador adicionaJogador(String nome){
		int id = -1;
		do{
			id = this.getRandomId();
		}while(this.jogadores.containsKey(id));
		
		Jogador j = new Jogador();
		j.nome = nome;
		j.id = id;
		
		j.comecouEsperarPorPartida = Calendar.getInstance().getTime().getTime();
		this.jogadores.put(id,j);
		return j;
	}
	
	
	// Deixa o jogador liberado para outra partida
	private void liberaJogador(Jogador jogador){
		jogador.estaOcupado = false;
		jogador.ehCabra = false;
		jogador.comecouEsperarPorPartida = Calendar.getInstance().getTime().getTime();
		this.jogadoresEsperando.put(jogador.id, jogador);
	}
	
	private void criarAdicionarPartida(Jogador j,Jogador j2){
		j = this.jogadores.get(j.id);
		j.ehCabra = true;
		j.estaOcupado = true;
		this.jogadores.put(j.id,j);
		j2 = this.jogadores.get(j2.id);
		j2.ehCabra = false;
		j2.estaOcupado = true;
		this.jogadores.put(j2.id,j2);
		int id = -1;
		do{
			id = this.getRandomId();
		}while(this.partidas.containsKey(id));
		
		Partida p = new Partida(id,j,j2);
		this.partidas.put(id,p);
		this.indiceJogadorIdParaPartidaId.put(j.id,p.id);
		this.indiceJogadorIdParaPartidaId.put(j2.id,p.id);
		this.jogadoresEsperando.remove(j.id);
		this.jogadoresEsperando.remove(j2.id);
	}
}
