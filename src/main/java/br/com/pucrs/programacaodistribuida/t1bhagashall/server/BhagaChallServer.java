package br.com.pucrs.programacaodistribuida.t1bhagashall.server;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class BhagaChallServer {
	public static void main (String[] args) {
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");			
		} catch (RemoteException e) {
			System.out.println("RMI registry already running.");			
		}
		try {
			Naming.rebind ("BhagaChall", new BhagaChall ());
			System.out.println ("BahgaChallServer is ready.");
		} catch (Exception e) {
			System.out.println ("BahgaChallServer failed:");
			e.printStackTrace();
		}
	}
}
