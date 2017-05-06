package br.com.pucrs.programacaodistribuida.t1bhagashall.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.com.pucrs.programacaodistribuida.t1bhagashall.game.BhagaChallGame;
import br.com.pucrs.programacaodistribuida.t1bhagashall.libs.BhagaChallInterface;

public class BhagaChall extends UnicastRemoteObject implements BhagaChallInterface{
	
	private static final long serialVersionUID = -513804057617910473L;
	
	
	public BhagaChall() throws RemoteException{

	}
	public int preRegistro(String nome1, int id1, String nome2, int id2) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int registraJogador(String nome) throws RemoteException {
		return BhagaChallGame.controle.registraJogador(nome);
	}

	public int encerraPartida(int id) throws RemoteException {
		return BhagaChallGame.controle.encerraPartida(id);
	}

	public int temPartida(int id) throws RemoteException {
		return BhagaChallGame.controle.temPartida(id);
	}

	public int ehMinhaVez(int id) throws RemoteException {
		return BhagaChallGame.controle.ehMinhaVez(id);
	}

	public String obtemGrade(int id) throws RemoteException {
		return BhagaChallGame.controle.obtemGrade(id);
	}

	public int moveTigre(int id, int tigre, int direcao) throws RemoteException {
		return BhagaChallGame.controle.moveTigre(id, tigre, direcao);
	}

	public int posicionaCabra(int id, int x, int y) throws RemoteException {
		return BhagaChallGame.controle.posicionaCabra(id, x, y);
	}

	public int moveCabra(int id, int cabra, int direcao) throws RemoteException {
		return BhagaChallGame.controle.moveCabra(id, cabra, direcao);
	}

	public String obtemOponente(int id) throws RemoteException {
		return BhagaChallGame.controle.obtemOponente(id);
	}

}
