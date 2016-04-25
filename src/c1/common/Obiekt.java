package c1.common;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.paukov.combinatorics.*;
import org.paukov.combinatorics.combination.simple.SimpleCombinationGenerator;

public class Obiekt {

	private Hashtable<Integer, Integer> atrybuty;
	private int klasaDecyzyjna;
	private boolean czyPokrytyLEM = false;
	private boolean czyUzywanyLEM = true;

	public Obiekt(String linia) {
		// tworzenie obiektu z pojedynczej lini z pliku z systemem decyzyjnym
		String[] pocieta = linia.split(" ");
		int dlugosc = pocieta.length;
		this.klasaDecyzyjna = Integer.parseInt(pocieta[dlugosc - 1]);
		Hashtable<Integer, Integer> atrybuty = new Hashtable<Integer, Integer>();
		for (int i = 0; i < dlugosc - 1; i++) {
			atrybuty.put(i, Integer.parseInt(pocieta[i]));
		}
		this.atrybuty = atrybuty;

	}
	
	public boolean getCzyPokrytyLEM(){
		return this.czyPokrytyLEM;
	}
	
	public void setCzyPokrytyLEM(boolean czyPokryty){
		this.czyPokrytyLEM = czyPokryty;
	}

	public int getKlasaDecyzyjna() {
		return this.klasaDecyzyjna;
	}

	public Hashtable<Integer, Integer> getAtrybuty() {
		return this.atrybuty;
	}
	//zwraca liste z hashtablami zawierajacymi wszystkie kombinacje atrybutow tego obiektu o danej dlugosci
	public List<Hashtable<Integer, Integer>> generujKombinacje(int dlugosc) {
		Integer[] indeksy = new Integer[this.atrybuty.size()];
		for (int i = 0; i < indeksy.length; i++) {
			indeksy[i] = i;
		}

		CombinatoricsVector<Integer> listaIndeksow = new CombinatoricsVector<Integer>(indeksy);
		Generator<Integer> generator = new SimpleCombinationGenerator<Integer>(listaIndeksow, dlugosc);
		Iterator<ICombinatoricsVector<Integer>> iterator = generator.iterator();

		List<Hashtable<Integer, Integer>> kombinacje = new ArrayList<Hashtable<Integer, Integer>>();
		while (iterator.hasNext()) {
			CombinatoricsVector<Integer> wektorKombinacji = (CombinatoricsVector<Integer>) iterator.next();
			
			Hashtable<Integer, Integer> kombinacja = new Hashtable<Integer, Integer>(dlugosc);
			for (int i = 0; i < dlugosc; i++) {
				kombinacja.put(wektorKombinacji.getValue(i), this.atrybuty.get(wektorKombinacji.getValue(i)));
			}
			kombinacje.add(kombinacja);
		}
		return kombinacje;
	}

	// zwraca true jezeli obiekty naleza do roznych klas decyzyjnych
	public static boolean rozniaSieKlasa(Obiekt ob1, Obiekt ob2) {
		if (ob1.getKlasaDecyzyjna() != ob2.getKlasaDecyzyjna()) {
			return true;
		} else {
			return false;
		}
	}


	

	@Override
	public String toString() {
		String wynik = "";
		for (int i = 0; i < atrybuty.size(); i++) {
			wynik += "  " + atrybuty.get(i) + " ";
		}
		wynik += "  " + this.klasaDecyzyjna;
		return wynik;
	}

	public boolean getCzyUzywanyLEM() {
		return czyUzywanyLEM;
	}

	public void setCzyUzywanyLEM(boolean czyUzywanyLEM) {
		this.czyUzywanyLEM = czyUzywanyLEM;
	}
}
