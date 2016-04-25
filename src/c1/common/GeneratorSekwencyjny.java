package c1.common;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public class GeneratorSekwencyjny {
	private SystemDecyzyjny sd;
	private List<Regula> reguly = new ArrayList<Regula>();
	private Hashtable<Integer, Boolean> czyWykluczony = new Hashtable<Integer, Boolean>();

	public GeneratorSekwencyjny(SystemDecyzyjny sd) {
		this.sd = sd;
		wypelnijCzyWykluczony();
		generujReguly();
	}

	private void generujReguly() {
		// przejscie po rzedach regul
		for (int i = 0; i < this.sd.getIloscAtrybutow(); i++) {
			// przejscie po wszystkich obiektach
			for (int j = 0; j < this.sd.getObiekty().size(); j++) {
				//sprawdzenie czy obiekt nie jest wykluczony z rozwazan
				if (!czyWykluczony.get(j)) {
					List<Hashtable<Integer, Integer>> kombinacje = this.sd.getObiekty().get(j).generujKombinacje(i);
					// przejscie po wszystkich kombinacjach
					for (int k = 0; k < kombinacje.size(); k++) {
						if(!czySprzeczna(j,kombinacje.get(k))){
							Regula r = new Regula(kombinacje.get(k),this.sd.getObiekty().get(j).getKlasaDecyzyjna(),j);
							wykluczObiektyZawierajace(r);
							this.reguly.add(r);
							break;
						}
					}
				}
			}
		}
	}
	
	private void wykluczObiektyZawierajace(Regula r){
		this.czyWykluczony.put(r.getNrObiektu(), true);
		for (int i = 0; i < this.czyWykluczony.size(); i++) {
			if(i!=r.getNrObiektu() && !Obiekt.rozniaSieKlasa(this.sd.getObiekty().get(i), this.sd.getObiekty().get(r.getNrObiektu()))){
				if(czyZawieraKombinacje(r.getPary(),this.sd.getObiekty().get(i))){
					czyWykluczony.put(i, true);
					r.zwiekszSupport();
				}
			}
		}
	}
	
	private boolean czySprzeczna(int nrObiektu, Hashtable<Integer, Integer> kombinacja){
		for (int i = 0; i < this.sd.getObiekty().size(); i++) {
			if(Obiekt.rozniaSieKlasa(this.sd.getObiekty().get(nrObiektu), this.sd.getObiekty().get(i)) && czyZawieraKombinacje(kombinacja,this.sd.getObiekty().get(i))){
				return true;
			}
		}
		return false;
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

	private void wypelnijCzyWykluczony() {
		for (int i = 0; i < this.sd.getObiekty().size(); i++) {
			this.czyWykluczony.put(i, false);
		}
	}
	
	@Override
	public String toString(){
		String wynik = "";
		wynik+= "--------------------------------------------------------\n";
		wynik+= "Algorytm sekwencyjnie pokrywajacy\n";
		wynik+= "--------------------------------------------------------\n";
		for (int i = 0; i < reguly.size(); i++) {
			wynik += reguly.get(i).toString() + "\n";
		}
		wynik+= "--------------------------------------------------------\n";
		return wynik;
	}
}
