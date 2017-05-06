package br.com.pucrs.programacaodistribuida.t1bhagashall.models;

public class Peca {
	public boolean ehCabra;
	public int id;
	public int x;
	public int y;
	
	public String toString(){
		if(this.ehCabra){
			return ""+(char)(this.id+65);
		}else{
			return ""+this.id;
		}
	}
}
