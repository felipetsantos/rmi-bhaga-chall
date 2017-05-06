package br.com.pucrs.programacaodistribuida.t1bhagashall.models;

import java.util.HashMap;
import java.util.Map;

public class Tabuleiro {
	
	public Casa[][] casas;
	public Map<Integer,Peca> cabras;
	public Map<Integer,Peca> tigres;
	public int numeroDeCabras;
	public int cabrasCapturadas;
	
	public Tabuleiro(){
		this.cabras = new HashMap<Integer, Peca>();
		this.tigres = new HashMap<Integer, Peca>();
		this.inicializaTabuleiro();
		this.colocaTigres();
		this.numeroDeCabras = 0;
		this.cabrasCapturadas = 0;
	}
	
	public String toString(){
		String tabuleiro = "";
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				if(this.casas[i][j].peca == null){
					tabuleiro +="."; 
				}else{
					tabuleiro +=this.casas[i][j].peca.toString();
				}
			}
		}
		return tabuleiro;
	}
	
	// Colocar a cabra na posição indicada assumindo que a posição existe e está livre
	public void posicionaCabra(int x,int y){
		Peca peca = new Peca();
		peca.ehCabra = true;
		peca.id = this.numeroDeCabras;
		this.casas[y][x].peca = peca;
		peca.y = y;
		peca.x = x;
		this.cabras.put(peca.id,peca);
		this.numeroDeCabras++;
	}
	// Verifica se a posição indicada não tem nenhuma peça
	public boolean posicaoEstaVazia(int x,int y){
		if(this.casas[y][x].peca == null){
			return true;
		}else{
			return false;
		}
	}
	
	// Verifica se a posição indicada está dentro do tabuleiro
	public boolean posicaoEhValida(int x,int y){
		if(x >=0 && x<=4 && y >= 0 &&  y <=4){
			return true;
		}else{
			return false;
		}
	}
	
	// Verifica se direção existe de acordo com o jogo
	public boolean aDirecaoExisteNoJogo(int codigo){
		if(TigreCabraDirecoesEnum.getDirecao(codigo) != null){
			return true;
		}else{
			return false;
		}
	}
	
	// Verifica se o tigre pode pular em uma determinada direção
	public boolean podePular(int tigreId,int codigoDirecao){
		Peca peca = this.tigres.get(tigreId);
		if(peca != null){
			Casa casaPulo = this.casas[peca.y][peca.x].pulosPossiveisParaTigres.get(codigoDirecao);
			Casa casaMovimentoSimples = this.casas[peca.y][peca.x].movimentosSimplesPossiveis.get(codigoDirecao);
			if(casaPulo != null && casaPulo.peca == null && casaMovimentoSimples != null && casaMovimentoSimples.peca != null && casaMovimentoSimples.peca.ehCabra){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	// Verifica se a cabra pode ser movida
	public boolean podeMoverCabra(int cabraId,int codigoDirecao){
		Peca peca = this.cabras.get(cabraId);
		if(peca != null){
			Casa casa = this.casas[peca.y][peca.x].movimentosSimplesPossiveis.get(codigoDirecao);
			if(casa != null && casa.peca == null){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	// Verifica se o tigre pode ser movido
	public boolean podeMoverTigre(int tigreId,int codigoDirecao){
		Peca peca = this.tigres.get(tigreId);
		if(peca != null){
			Casa casa = this.casas[peca.y][peca.x].movimentosSimplesPossiveis.get(codigoDirecao);
			if(casa != null && casa.peca == null){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	// Move o tigre para posição 
	public boolean moveTigre(int tigreId,int codigoDirecao){
		Peca peca = this.tigres.get(tigreId);
		if(peca != null){
			Casa casaPulo = this.casas[peca.y][peca.x].pulosPossiveisParaTigres.get(codigoDirecao);
			Casa casaMovimentoSimples = this.casas[peca.y][peca.x].movimentosSimplesPossiveis.get(codigoDirecao);
			if(casaPulo != null && casaPulo.peca == null && casaMovimentoSimples != null && casaMovimentoSimples.peca != null && casaMovimentoSimples.peca.ehCabra){
				// pula
				int oldX = peca.x;
				int oldY = peca.y;
				peca.x = casaPulo.x;
				peca.y = casaPulo.y;
				this.tigres.put(peca.id, peca);
				this.casas[oldY][oldX].peca = null;
				this.capturaCabra(casaMovimentoSimples.peca);
				this.casas[peca.y][peca.x].peca = peca;
				return true;
			}else if(casaMovimentoSimples != null && casaMovimentoSimples.peca == null){
				// movimenta
				int oldX = peca.x;
				int oldY = peca.y;
				peca.x = casaMovimentoSimples.x;
				peca.y = casaMovimentoSimples.y;
				this.tigres.put(peca.id, peca);
				this.casas[oldY][oldX].peca = null;
				this.casas[peca.y][peca.x].peca = peca;
				return true;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}

	// Move o cabra para posição 
	public boolean moveCabra(int cabraId,int codigoDirecao){
		Peca peca = this.cabras.get(cabraId);
		if(peca != null){
			Casa casaMovimentoSimples = this.casas[peca.y][peca.x].movimentosSimplesPossiveis.get(codigoDirecao);

			if(casaMovimentoSimples != null && casaMovimentoSimples.peca == null){
				// movimenta
				int oldX = peca.x;
				int oldY = peca.y;
				peca.x = casaMovimentoSimples.x;
				peca.y = casaMovimentoSimples.y;
				this.cabras.put(peca.id, peca);
				this.casas[oldY][oldX].peca = null;
				this.casas[peca.y][peca.x].peca = null;
				return true;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	
	public boolean capturaCabra(Peca peca){
		this.cabras.remove(peca.id);
		this.casas[peca.y][peca.y].peca = null;
		this.cabrasCapturadas++;
		return true;
	}
	//##### métodos auxiliares
	
	// Inicializa o tabuleiro e dados de apoio
	private void inicializaTabuleiro(){
		
		this.casas = new Casa[5][5];
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				this.casas[i][j] = new Casa(null);
				this.casas[i][j].x = j;
				this.casas[i][j].y = i;
			}
		}
		this.colocaTigres();
		
		// inicializa dados de apoio para verificações de movimentos válidos
		this.linha1InicializaMovimentos();
		this.linha2InicializaMovimentos();
		this.linha3InicializaMovimentos();
		this.linha4InicializaMovimentos();
		this.linha5InicializaMovimentos();
	}
	
	private void linha1InicializaMovimentos(){
		int x,y;
		// 0,0
		y=0;
		x=0; 

		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[0][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[1][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[1][0]);
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[0][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[2][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[2][0]);
		
		// 0,1
		x=1; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[0][0]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[0][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[1][1]);
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[0][3]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[2][1]);
		
		// 0,2
		x=2; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[0][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[0][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[1][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[1][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[1][1]);

		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[0][0]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[0][4]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[2][4]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[2][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[2][0]);
		// 0,3
		x=3; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[0][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[0][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[1][3]);

		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[0][1]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[2][3]);
		
		// 0,4
		x=4; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[0][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[1][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[1][3]);
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[0][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[2][4]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[2][2]);
	}
	
	private void linha2InicializaMovimentos(){
		int x,y;
		// 1,0
		y=1;
		x=0; 

		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[0][0]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[1][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[2][0]);
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[1][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[3][0]);
		// 1,1
		x=1; 
		
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[1][0]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[0][0]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[0][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[0][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[1][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[2][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[2][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[2][0]);

	
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[1][3]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[3][3]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[3][1]);
		// 1,2
		x=2; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[1][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[0][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[1][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[2][2]);
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[1][0]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[1][4]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[3][2]);
		
		// 1,3
		x=3; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[1][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[0][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[0][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[0][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[1][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[2][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[2][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[2][2]);

		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[1][1]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[3][3]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[3][1]);
		// 1,4
		x=4; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[1][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[0][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[2][4]);

		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[1][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[3][4]);
	}
	
	private void linha3InicializaMovimentos(){
		int x,y;
		// 2,0
		y=2;
		x=0; 

		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[1][0]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[1][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[2][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[3][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[3][0]);
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[0][0]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[0][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[2][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[4][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[4][0]);
		
		// 2,1
		x=1; 
		
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[1][0]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[0][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[1][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[2][1]);


		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[0][1]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[2][3]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[4][1]);
		
		// 2,2
		x=2; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[2][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[1][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[1][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[1][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[2][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[3][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[3][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[3][1]);

		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[4][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[0][0]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[0][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[0][4]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[2][4]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[4][4]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[4][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[4][0]);
		// 2,3
		x=3; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[2][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[1][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[2][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[3][3]);

		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[2][1]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[0][3]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[4][3]);
		// 2,4
		x=4; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[2][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[1][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[1][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[3][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[3][3]);

		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[2][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[0][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[0][4]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[4][4]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[4][2]);
	}
	
	private void linha4InicializaMovimentos(){
		int x,y;
		// 3,0
		y=3;
		x=0; 

		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[2][0]);;
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[3][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[4][0]);
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[1][0]);;
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[3][2]);
		
		// 3,1
		x=1; 
		
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[3][0]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[2][0]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[2][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[2][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[3][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[4][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[4][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[4][0]);


		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[1][1]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[1][3]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[3][3]);
		
		// 3,2
		x=2; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[3][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[2][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[3][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[4][2]);

		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[3][0]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[1][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[3][4]);

		// 3,3
		x=3; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[3][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[2][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[2][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[2][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[3][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_BAIXO.getCodigo(), this.casas[4][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[4][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_E_PARA_BAIXO.getCodigo(), this.casas[4][2]);

		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[3][1]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[1][1]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[1][3]);
		
		// 3,4
		x=4; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[3][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[2][4]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_BAIXO.getCodigo(), this.casas[4][4]);
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[3][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[1][4]);


	}
	
	private void linha5InicializaMovimentos(){
		int x,y;
		// 4,0
		y=4;
		x=0; 

		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[3][0]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[3][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[4][1]);
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[2][0]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[2][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[4][2]);
		
		// 4,1
		x=1; 
		
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[4][0]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[3][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[4][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[4][0]);
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[2][1]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[4][3]);
		// 4,2
		x=2; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[4][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[3][1]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[3][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[3][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[4][3]);

		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[4][0]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[2][0]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[2][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA_E_PARA_CIMA.getCodigo(), this.casas[2][4]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[4][4]);
		
		// 4,3
		x=3; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[4][2]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[3][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_DIREITA.getCodigo(), this.casas[4][4]);
		
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[4][1]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[2][3]);

		// 4,4
		x=4; 
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[4][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[3][3]);
		this.casas[y][x].movimentosSimplesPossiveis.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[3][4]);

		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA.getCodigo(), this.casas[4][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_ESQUERDA_PARA_CIMA.getCodigo(), this.casas[2][2]);
		this.casas[y][x].pulosPossiveisParaTigres.put(TigreCabraDirecoesEnum.PARA_CIMA.getCodigo(), this.casas[2][4]);

	}
	
	// Posiciona os tigres na posição inicial
	private void colocaTigres(){
		Peca[] pecas = new Peca[4];
		for(int i=1;i<=4;i++){
			pecas[i-1] = new Peca();
			pecas[i-1].ehCabra = false;
			pecas[i-1].id = i;
			this.tigres.put(pecas[i-1].id, pecas[i-1]);
		}

		this.casas[0][0].peca = pecas[0];
		this.casas[0][0].peca.y = 0;
		this.casas[0][0].peca.x = 0;
		
		
		this.casas[0][4].peca = pecas[1];
		this.casas[0][4].peca.y = 0;
		this.casas[0][4].peca.x = 4;
		
		
		this.casas[4][0].peca = pecas[2];
		this.casas[4][0].peca.y = 4;
		this.casas[4][0].peca.x = 0;
		
		this.casas[4][4].peca = pecas[3];
		this.casas[4][4].peca.y = 4;
		this.casas[4][4].peca.x = 4;
	}
	
	public boolean verificaSeOsTigresGanharam(){
		if(this.numeroDeCabras == 20 && this.cabrasCapturadas == 20){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean verificaSeAsCabrasGanharam(){
		
		for(Peca peca: this.tigres.values()){
			for(Map.Entry<Integer, Casa> entry :this.casas[peca.y][peca.x].movimentosSimplesPossiveis.entrySet()){
				if(this.podeMoverTigre(peca.id, entry.getKey()) || this.podePular(peca.id, entry.getKey())){
					return false;
				}
			}
		}
		return true;
	}
}
