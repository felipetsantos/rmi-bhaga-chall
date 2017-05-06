package br.com.pucrs.programacaodistribuida.t1bhagashall.models;

import java.util.Calendar;
import java.util.Date;

public class Jogador {
	public int id;
	public String nome;
	public boolean estaOcupado;
	public boolean ehCabra;
	public long comecouEsperarPorPartida;
	
	public Jogador(){
		this.ehCabra = false;
		this.estaOcupado = false;
		this.comecouEsperarPorPartida = Calendar.getInstance().getTime().getTime();
	}
	
	public boolean aindaDeveEsperarPorPartida(){
		Date d1 = new Date(this.comecouEsperarPorPartida);
		Date d2 = Calendar.getInstance().getTime();
		long seconds = (d2.getTime()-d1.getTime())/1000;
		if(seconds >= 120){
			return false;
		}else{
			return true;
		}
	}
	
}
