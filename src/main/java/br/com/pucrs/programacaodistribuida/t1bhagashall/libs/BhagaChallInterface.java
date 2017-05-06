package br.com.pucrs.programacaodistribuida.t1bhagashall.libs;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BhagaChallInterface extends Remote {
	public int preRegistro(String nome1,int id1, String nome2, int id2) throws RemoteException;
	public int registraJogador(String nome) throws RemoteException;
	public int encerraPartida(int id) throws RemoteException;
	public int temPartida(int id) throws RemoteException;
	public int ehMinhaVez(int id) throws RemoteException;
	public String obtemGrade(int id) throws RemoteException;
	public int moveTigre(int id, int tigre, int direcao) throws RemoteException;;
	public int posicionaCabra(int id, int x,int y) throws RemoteException;;
	public int moveCabra(int id, int cabra, int direcao) throws RemoteException;
	public String obtemOponente(int id) throws RemoteException;
}
