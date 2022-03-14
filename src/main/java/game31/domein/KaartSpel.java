package game31.domein;

import java.util.*;

public class KaartSpel
{
	private Vector kaarten = new Vector();
	private KaartStapel ks;
	private Kaart k;

	public KaartSpel(KaartStapel ks)
	{
		this.ks = ks;
		String s = ks.geefSpelType();
		if(s.equals("Eenendertigen")) {
			for(int i=7; i<15; i++) {
				String line = "" + i;
				int waarde;
				if(i == 11 || i == 12 || i == 13) {
					waarde = 10;
				}
				else if(i == 14) {
					waarde = 11;
				}
				else {
					waarde = i;
				}

				k = new Kaart("game31/kaarten/d" + line +".jpg", "ruiten", line, waarde, this, ks);
				kaarten.add(k);
			}
			for(int i=7; i<15; i++) {
		    	String line = "" + i;
				int waarde;
				if(i == 11 || i == 12 || i == 13) {
					waarde = 10;
				}
				else if(i == 14) {
					waarde = 11;
				}
				else {
					waarde = i;
				}
				k = new Kaart("game31/kaarten/h" + line +".jpg", "harten", line, waarde, this, ks);
				kaarten.add(k);
			}
			for(int i=7; i<15; i++) {
				String line = "" + i;
				int waarde;
				if(i == 11 || i == 12 || i == 13) {
					waarde = 10;
				}
				else if(i == 14) {
					waarde = 11;
				}
				else {
					waarde = i;
				}
				k = new Kaart("game31/kaarten/k" + line +".jpg", "klaveren", line, waarde, this, ks);
				kaarten.add(k);
			}
			for(int i=7; i<15; i++) {
				String line = "" + i;
				int waarde;
				if(i == 11 || i == 12 || i == 13) {
					waarde = 10;
				}
				else if(i == 14) {
					waarde = 11;
				}
				else {
					waarde = i;
				}
				k = new Kaart("game31/kaarten/s" + line +".jpg", "schoppen", line, waarde, this, ks);
				kaarten.add(k);
			}

		}
		//System.out.println(kaarten.size());

	}


	public Vector geefKaarten()
	{
		return kaarten;
	}
}
