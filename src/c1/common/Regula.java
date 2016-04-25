package c1.common;

import java.util.Hashtable;
import java.util.Set;

public class Regula {

	private Hashtable<Integer, Integer> pary;
	private int klasaDecyzyjna;
	private int nrObiektu;
	private int support;

	public Regula(){
		this.pary = new Hashtable<Integer, Integer>();
		this.support = 0;
	}
	
	public Regula(Hashtable<Integer, Integer> pary, int klasa) {
		this.pary = pary;
		this.klasaDecyzyjna = klasa;
	}
	
	public Regula(Hashtable<Integer, Integer> pary, int klasa, int nrObiektu) {
		this.pary = pary;
		this.klasaDecyzyjna = klasa;
		this.nrObiektu = nrObiektu;
		this.support = 1;
	}
	
	public void setKlasaDecyzyjna(int klasa){
		this.klasaDecyzyjna = klasa;
	}

	
	public Hashtable<Integer, Integer> getPary() {
		return this.pary;
	}
	
	public void zwiekszSupport(){
		this.support+=1;
	}


	public int getNrObiektu(){
		return this.nrObiektu;
	}
	
	public int getKlasaDecyzyjna() {
		return this.klasaDecyzyjna;
	}

	@Override
	public String toString() {
		String result = "";
		Set<Integer> keys = this.pary.keySet();
		int rozmiar = keys.size();
		int licznik = 0;
			for (Integer key : keys) {

				if (keys.size() == 1) {
					result += "(a" + (key + 1) + "=" + pary.get(key) + ") ";
				} 
				else if(licznik == rozmiar-1){
					result += "(a" + (key + 1) + "=" + pary.get(key) + ") ";
				}
				else{
					result += "(a" + (key + 1) + "=" + pary.get(key) + ") & ";
				}
				licznik++;
			}
		
		result += "=> (d=" + this.getKlasaDecyzyjna() + ")";
		if(this.support>1){
			result+="[" + this.support + "]";
		}
		return result;
	}

	public int getSupport() {
		return support;
	}

	public void setSupport(int support) {
		this.support = support;
	}

	public void setNrObiektu(int nrObiektu) {
		this.nrObiektu = nrObiektu;
	}
}
