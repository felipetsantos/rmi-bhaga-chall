package br.com.pucrs.programacaodistribuida.t1bhagashall.models;

import java.util.Calendar;
import java.util.Date;

public class Partida {
	public int id;
	public Jogador jogador1;
	public Jogador jogador2;
	public long tempoDaUltimaJogada;
	public int quemFezAUltimaJogada;
	public int vencedorId;
	public Tabuleiro tabuleiro;
	public long tempoDeEncerramento;
	public String motivoPartidaAcabar;
	public Partida(int id,Jogador j1,Jogador j2) {
		this.jogador1 = j1;
		this.jogador2 = j2;
		this.id = id;
		this.tempoDaUltimaJogada = Calendar.getInstance().getTime().getTime();
		this.quemFezAUltimaJogada = -1;
		this.vencedorId = -1;
		this.tabuleiro = new Tabuleiro();
	}
	
	public boolean aindaDeveEsperarPorJogada(){
		Date d1 = new Date(this.tempoDaUltimaJogada);
		Date d2 = Calendar.getInstance().getTime();
		long seconds = (d2.getTime()-d1.getTime())/1000;
		if(seconds >= 30){
			return false;
		}else{
			return true;
		}
	}
	public boolean deveSerApagada(){
		Date d1 = new Date(this.tempoDeEncerramento);
		Date d2 = Calendar.getInstance().getTime();
		long seconds = (d2.getTime()-d1.getTime())/1000;
		if(seconds >= 60){
			return true;
		}else{
			return false;
		}
	}
	public boolean temVencedor(){
		if(this.vencedorId  == -1){
			return false;
		}else if(this.vencedorId  == 0){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean ehEmpate(){
		if(this.vencedorId  == 0){
			return true;
		}else{
			return false;
		}
	}
	public void encerraPartida(int id,String motivoEncerramento){
		this.tempoDeEncerramento = Calendar.getInstance().getTime().getTime();
		this.motivoPartidaAcabar = motivoEncerramento;
		if(this.jogador1.id == id){
			this.vencedorId = this.jogador2.id;
		}else{
			this.vencedorId = this.jogador1.id;
		}
	}
	public boolean ninguemJogou(){
		if(this.quemFezAUltimaJogada != -1){
			return true;
		}else{
			return false;
		}
	}
	
	public int moveTigre(Jogador j,int tigreId,int codigoDirecao){
		if(this.tabuleiro.aDirecaoExisteNoJogo(codigoDirecao)){
			if(!j.ehCabra){
					if(this.quemFezAUltimaJogada != j.id){
						if(this.tabuleiro.podePular(tigreId,codigoDirecao) || this.tabuleiro.podeMoverTigre(tigreId,codigoDirecao)){
							// posição valida
							this.tabuleiro.moveTigre(tigreId,codigoDirecao);
							this.quemFezAUltimaJogada = j.id;
							this.tempoDaUltimaJogada = Calendar.getInstance().getTime().getTime();
							this.verificaSeTemGanhador();
							return 1;
						}else{
							// movimento inválido
							return 0;
						}
					}else{
						// não é sua vez
						return -3;
					}
			}else{
				// não está jogando com o animal correto
				return -4;
			}
		}else{
			// direção inválida
			return -5;
		}
	
	}
	
	public boolean verificaSeTemGanhador(){
		boolean tigres = this.tabuleiro.verificaSeOsTigresGanharam();
		if(tigres){
			if(this.jogador1.ehCabra){
				this.encerraPartida(this.jogador1.id, "Todas as cabras foram capturadas");
			}else{
				this.encerraPartida(this.jogador2.id, "Todas as cabras foram capturadas");
			}	
		}
		boolean cabras = this.tabuleiro.verificaSeAsCabrasGanharam();
		if(cabras){
			if(this.jogador1.ehCabra){
				this.encerraPartida(this.jogador2.id, "Os tigres não podem mais se mover");
			}else{
				this.encerraPartida(this.jogador1.id, "Os tigres não podem mais se mover");
			}
			return true;
		}
		return false;
	}
	public int moveCabra(Jogador j,int cabraId,int codigoDirecao){
		if(this.tabuleiro.aDirecaoExisteNoJogo(codigoDirecao)){
			if(!j.ehCabra){
					if(this.quemFezAUltimaJogada != j.id){
						if(this.tabuleiro.podeMoverCabra(cabraId,codigoDirecao)){
							// posição valida
							this.tabuleiro.moveCabra(cabraId,codigoDirecao);
							this.quemFezAUltimaJogada = j.id;
							this.tempoDaUltimaJogada = Calendar.getInstance().getTime().getTime();
							this.verificaSeTemGanhador();
							return 1;
						}else{
							// movimento inválido
							return 0;
						}
					}else{
						// não é sua vez
						return -3;
					}
			}else{
				// não está jogando com o animal correto
				return -4;
			}
		}else{
			// direção inválida
			return -5;
		}		
	}
	public int posicionaCabra(Jogador j,int x,int y){
			if(j.ehCabra){
				if(this.tabuleiro.numeroDeCabras < 20){
					if(this.quemFezAUltimaJogada != j.id){
						if(this.tabuleiro.posicaoEhValida(x, y) && this.tabuleiro.posicaoEstaVazia(x, y)){
							// posição valida
							this.tabuleiro.posicionaCabra(x,y);
							this.quemFezAUltimaJogada = j.id;
							this.tempoDaUltimaJogada = Calendar.getInstance().getTime().getTime();
							this.verificaSeTemGanhador();
							return 1;
						}else{
							// movimento inválido
							return 0;
						}
					}else{
						// não é sua vez
						return -3;
					}
				}else{
					//todas as cabras já foram posicionadas
					return -5;
				}
			}else{
				// não está jogando com o animal correto
				return -4;
			}
		
	}
}
