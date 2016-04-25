package c1.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

public class GeneratorLEM {
	private SystemDecyzyjny sd;
	private List<Regula> reguly = new ArrayList<Regula>();
	private List<ArrayList<Obiekt>> koncepty = new ArrayList<ArrayList<Obiekt>>();
	
	public GeneratorLEM(SystemDecyzyjny sd){
		this.sd = sd;
		generujKoncepty();
		generujReguly();
		
		int w = 0;
		w = 1;
	}
	
	private void generujReguly(){
		//przejscie po konceptach
		for (int i = 0; i < koncepty.size(); i++) {
			generujRegulyDlaKonceptu(i);
		}
	}
	private void generujRegulyDlaKonceptu(int nr){

		while(niepokryteObiekty(koncepty.get(nr)).size()>0){
			ArrayList<Integer> uzyteAtrybuty = new ArrayList<Integer>();
			Map.Entry<Integer, Integer> najczestszyDeskryptor = najczestszyDeskryptor(niepokryteObiekty(koncepty.get(nr)),uzyteAtrybuty);
			uzyteAtrybuty.add(najczestszyDeskryptor.getKey());
			Hashtable<Integer, Integer> pary = new Hashtable<Integer,Integer>();
			pary.put(najczestszyDeskryptor.getKey(), najczestszyDeskryptor.getValue());
			while(czySprzeczna(pary, nr)){
				najczestszyDeskryptor = najczestszyDeskryptor(dobreObiekty(niepokryteObiekty(koncepty.get(nr)),pary),uzyteAtrybuty);
				uzyteAtrybuty.add(najczestszyDeskryptor.getKey());
				pary.put(najczestszyDeskryptor.getKey(), najczestszyDeskryptor.getValue());
			}
			Regula r = new Regula(pary, this.koncepty.get(nr).get(0).getKlasaDecyzyjna());
			pokryjObiekty(this.koncepty.get(nr),r);
			this.reguly.add(r);
		}
	}
	private void pokryjObiekty(ArrayList<Obiekt> koncept, Regula r){
		for (int i = 0; i < koncept.size(); i++) {
			if(czyZawieraKombinacje(r.getPary(), koncept.get(i))){
				koncept.get(i).setCzyPokrytyLEM(true);
				r.zwiekszSupport();
			}
		}
	}
	
	private ArrayList<Obiekt> dobreObiekty(ArrayList<Obiekt> niepokryte, Hashtable<Integer, Integer> pary){
		ArrayList<Obiekt> dobre = new ArrayList<Obiekt>();
		for (int i = 0; i < niepokryte.size(); i++) {
			if(czyZawieraKombinacje(pary, niepokryte.get(i))){
				dobre.add(niepokryte.get(i));
			}
		}
		return dobre;
	}
	
	private boolean czySprzeczna(Hashtable<Integer, Integer> pary, int nrKonceptu){
		for (int i = 0; i < this.koncepty.size(); i++) {
			if(i != nrKonceptu){
				for (int j = 0; j < this.koncepty.get(i).size(); j++) {
					if(czyZawieraKombinacje(pary, this.koncepty.get(i).get(j))){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	private Map.Entry najczestszyDeskryptor(ArrayList<Obiekt> koncept, ArrayList<Integer> uzyteAtrybuty){
		Hashtable<Map.Entry<Integer, Integer>, Integer> pozliczane = new Hashtable<Map.Entry<Integer, Integer>, Integer>();
		for (int i = 0; i < koncept.size(); i++) {
			Set<Map.Entry<Integer,Integer>> deskryptory = koncept.get(i).getAtrybuty().entrySet();
			Iterator<Map.Entry<Integer,Integer>> iterator = deskryptory.iterator();
			while(iterator.hasNext()){
				Map.Entry<Integer,Integer> deskryptor = iterator.next();
				if(!ArrayUtils.contains(uzyteAtrybuty.toArray(), deskryptor.getKey())){
					if(pozliczane.containsKey(deskryptor)){
						pozliczane.put(deskryptor, pozliczane.get(deskryptor)+1);
					}
					else{
						pozliczane.put(deskryptor, 1);
					}
				}
			}
		}
		
		ArrayList<Map.Entry<Integer, Integer>> najczestsze = new ArrayList<Map.Entry<Integer, Integer>>();
		Integer maxValue = Integer.MIN_VALUE; 
		for(Map.Entry<Map.Entry<Integer, Integer>, Integer> entry : pozliczane.entrySet()) {
		     if(entry.getValue() > maxValue) {
		         najczestsze.clear();
		         najczestsze.add(entry.getKey());
		         maxValue = entry.getValue();
		     }
		     else if(entry.getValue() == maxValue)
		     {
		       najczestsze.add(entry.getKey());
		     }
		}
		//indeks pierwszego deskryptora od lewej
		Integer indeksOdLewej = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Integer> deskryptor : najczestsze){
            indeksOdLewej = indeksOdLewej < deskryptor.getKey() ? indeksOdLewej : deskryptor.getKey();
        }
		//wybor pierwszego deskryptora od gory
        Map.Entry<Integer, Integer> najlepszy = null;
		outerloop:
        for (int i = 0; i < koncept.size(); i++) {
			Set<Map.Entry<Integer,Integer>> deskryptory = koncept.get(i).getAtrybuty().entrySet();
			for (int j = 0; j < najczestsze.size(); j++) {
				if(najczestsze.get(j).getKey() == indeksOdLewej && deskryptory.contains(najczestsze.get(j))){
					najlepszy = najczestsze.get(j);
					break outerloop;
				}
			}
        }
        return najlepszy;
        
	}
	
	private ArrayList<Obiekt> niepokryteObiekty(ArrayList<Obiekt> koncept){
		ArrayList<Obiekt> niepokryte = new ArrayList<Obiekt>();
		for (int i = 0; i < koncept.size(); i++) {
			if(!koncept.get(i).getCzyPokrytyLEM()){
				niepokryte.add(koncept.get(i));
			}
		}
		return niepokryte;
	}
	
	private boolean czyZawieraKombinacje(Hashtable<Integer, Integer> kombinacja, Obiekt ob){
		Set<Entry<Integer, Integer>> e1 = kombinacja.entrySet();
		Set<Entry<Integer, Integer>> e2 = ob.getAtrybuty().entrySet();
		if(e2.containsAll(e1)){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	private void generujKoncepty(){
		Set<Integer> unikalne = new HashSet<Integer>();
		for (int i = 0; i < this.sd.getObiekty().size(); i++) {
			unikalne.add(this.sd.getObiekty().get(i).getKlasaDecyzyjna());
		}
		List<Integer> klasyDecyzyjne = new ArrayList<Integer>(unikalne);
		
		for (int i = 0; i < klasyDecyzyjne.size(); i++) {
			ArrayList<Obiekt> koncept = new ArrayList<Obiekt>();
			for (int j = 0; j < this.sd.getObiekty().size(); j++) {
				if(this.sd.getObiekty().get(j).getKlasaDecyzyjna() == klasyDecyzyjne.get(i)){
					koncept.add(this.sd.getObiekty().get(j));
				}
			}
			this.koncepty.add(koncept);
					
		}

	}
	
	@Override
	public String toString(){
		String wynik = "";
		wynik+= "--------------------------------------------------------\n";
		wynik+= "Algorytm LEM2\n";
		wynik+= "--------------------------------------------------------\n";
		for (int i = 0; i < reguly.size(); i++) {
			wynik += reguly.get(i).toString() + "\n";
		}
		wynik+= "--------------------------------------------------------\n";
		return wynik;
	}
}
