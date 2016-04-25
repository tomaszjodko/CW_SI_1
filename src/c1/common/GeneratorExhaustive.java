package c1.common;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public class GeneratorExhaustive {
	private Hashtable<Integer, Hashtable<Integer, Hashtable<Integer, Integer>>> macierzNieodroznialnosci;
	private List<Regula> reguly = new ArrayList<Regula>();
	private SystemDecyzyjny sd;
	
	public GeneratorExhaustive(SystemDecyzyjny sd){
		this.sd = sd;
		generujMacierzNieodr();
		generujReguly();
	}

	
	private void generujMacierzNieodr() {
		int wymiar = this.sd.getObiekty().size();
		Hashtable<Integer, Hashtable<Integer, Hashtable<Integer, Integer>>> macierz = new Hashtable<Integer, Hashtable<Integer, Hashtable<Integer, Integer>>>(
				wymiar);
		// przejscie po wszystkich obiektach
		for (int i = 0; i < wymiar; i++) {
			macierz.put(i, new Hashtable<Integer, Hashtable<Integer, Integer>>(wymiar));
			// porownanie obiektow
			for (int j = 0; j < wymiar; j++) {
				if (Obiekt.rozniaSieKlasa(this.sd.getObiekty().get(i), this.sd.getObiekty().get(j))) {
					macierz.get(i).put(j, porownajObiekty(this.sd.getObiekty().get(i), this.sd.getObiekty().get(j)));
				}
			}
		}
		this.macierzNieodroznialnosci = macierz;
	}
	
	
	private void generujReguly() {
		// przejscie po kolejnych rzedach regul
		for (int i = 1; i <= this.sd.getIloscAtrybutow(); i++) {
			// przejscie po wszystkich obiektach
			for (int j = 0; j < this.sd.getObiekty().size(); j++) {
				// wygenerowanie wszystkich kombinacji artybutow obiektu o
				// numerze j o danej dlugosci i
				List<Hashtable<Integer, Integer>> kombinacje = this.sd.getObiekty().get(j).generujKombinacje(i);
				// przejscie po wszystkich kombinacjach
				for (int k = 0; k < kombinacje.size(); k++) {
					Set<Entry<Integer, Integer>> e1 = kombinacje.get(k).entrySet();

					boolean nadajeSieNaRegule = true;
					// przejscie po wszystkich rzedach macierzy
					// nieodroznialnosci
					for (int l = 0; l < this.macierzNieodroznialnosci.size(); l++) {
						// przejscie po wszystkich komorkach w danym rzedzie
						// macierzy nieodroznialnosci
						Set<Integer> keys = this.macierzNieodroznialnosci.get(l).keySet();
						for (Integer key : keys) {
							//sprawdza czy dana kombinacja zawiera sie w danej komorce macierzy nieodroznialnosci
							Set<Entry<Integer, Integer>> e2 = this.macierzNieodroznialnosci.get(l).get(key).entrySet();
							if (e2.containsAll(e1)) {
								nadajeSieNaRegule = false;
								break;
							}
						}
					}
					if (nadajeSieNaRegule) {
						Regula kandydat = new Regula(kombinacje.get(k), this.sd.getObiekty().get(j).getKlasaDecyzyjna(),j);
						if (!zawieraInneReguly(kandydat)) {
							this.reguly.add(kandydat);
						}
					}
				}
			}
		}
	}
	
	private boolean zawieraInneReguly(Regula r) {
		for (int i = 0; i < this.reguly.size(); i++) {
			if (zawiera(r, this.reguly.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	// sprawdza czy dana regula zawiera sie w drugiej, jesli sa takie same to zwieksza support reguly ktora juz znajduje sie na liscie
		private boolean zawiera(Regula r1, Regula r2) {
			Set<Entry<Integer, Integer>> e1 = r1.getPary().entrySet();
			Set<Entry<Integer, Integer>> e2 = r2.getPary().entrySet();
			if (e1.containsAll(e2)) {
				if(e1.equals(e2)){
					r2.zwiekszSupport();
				}
				return true;
			} else {
				return false;
			}
		}
		
		// zwraca hashtable z atrybutami na podstawie ktorych nie mozna odroznic
		// obiektow
		private Hashtable<Integer, Integer> porownajObiekty(Obiekt ob1, Obiekt ob2) {
			Hashtable<Integer, Integer> wynik = new Hashtable<Integer, Integer>();
			for (int i = 0; i < ob1.getAtrybuty().size(); i++) {
				if (ob1.getAtrybuty().get(i) == ob2.getAtrybuty().get(i)) {
					wynik.put(i, ob1.getAtrybuty().get(i));
				}
			}
			return wynik;
		}

	
	@Override
	public String toString(){
		String wynik = "";
		wynik+= "--------------------------------------------------------\n";
		wynik+= "Algorytm exhaustive\n";
		wynik+= "--------------------------------------------------------\n";
		for (int i = 0; i < reguly.size(); i++) {
			wynik += reguly.get(i).toString() + "\n";
		}
		wynik+= "--------------------------------------------------------\n";
		return wynik;
	}
}
