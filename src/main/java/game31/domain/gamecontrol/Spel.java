package game31.domain.gamecontrol;

import game31.domain.carddeck.facade.CardDeckServices;
import game31.domain.carddeck.facade.KaartDTO;
import game31.domain.players.facade.PlayersService;
import game31.userinterface.MainFrame;
import game31.userinterface.ScoreOverzichtFrame;

import java.awt.Frame;
import java.util.*;

import javax.swing.JOptionPane;

public class Spel
{
	private String spelNaam = "Eenendertigen";
	private Frame mf;
	private Vector<SpelRonde> spelRondes = new Vector<SpelRonde>();
	private int	gewonnenSpeler;
	private TreeSet<Integer> spelerIds = new TreeSet<Integer>();
	private TreeSet<Integer> deelnemendeSpelerIds = new TreeSet<Integer>();
	private	Tafel tafel = new Tafel();

	public Spel() {
	}

	public Spel(Vector<?> compSpeler, Vector<?> humanSpelerData, int fiches, Frame mf)
	{
		this.mf = mf;
		if (!humanSpelerData.isEmpty()) {
			for (Iterator<?> i = humanSpelerData.iterator(); i.hasNext();) {
				Vector<?> data = (Vector<?>) i.next();
				for (Iterator<?> j = data.iterator(); j.hasNext();) {
					spelerIds.add(PlayersService.getInstance().createHumanSpeler((String) j.next(), (Calendar) j.next(), fiches));
				}
			}
		}
		if (!compSpeler.isEmpty()) {
			for (Iterator<?> i = compSpeler.iterator(); i.hasNext();) {
				spelerIds.add(PlayersService.getInstance().createComputerSpeler((String) i.next(), fiches));
			}
		}
		deelnemendeSpelerIds = spelerIds;

		CardDeckServices.createCardDeckServices(this);
		SpelRonde sr = new SpelRonde(this, tafel, spelerIds);
		sr.setSpeler(spelerIds.first());
		spelRondes.add(sr);
	}

	public String getNaam() {
		return spelNaam;
	}
	
	public void ruil1Kaart(KaartDTO k1, KaartDTO k2)
	{
		SpelRonde spelronde = (SpelRonde) spelRondes.lastElement();
		spelronde.getActiveDeelname().replaceFor(k1, k2);
		tafel.replaceFor(k2, k1);
		if (controleerWaarde(getHuidigeSpelRonde().getActiveDeelname()) == 31) {
			JOptionPane.showMessageDialog(mf,"U heef 31, een nieuwe SpelRonde word gestart.", "Error", JOptionPane.ERROR_MESSAGE);
			volgendeRonde();
		}
		else getHuidigeSpelRonde().volgendeSpeler();
		updateUI();
	}
	
	public void updateUI() {
		if(mf instanceof MainFrame) {
			MainFrame frame = (MainFrame) mf;
			frame.updateUI();
		}
	}

	public void ruil3Kaart()
	{
		SpelRonde spelronde = (SpelRonde) spelRondes.lastElement();
		Vector<KaartDTO> tafel1 = tafel.getKaarten();
		Vector<KaartDTO> dn = spelronde.getActiveDeelname().getKaarten();
		spelronde.getActiveDeelname().replaceAll(tafel1);
		tafel.replaceAll(dn);
		if (controleerWaarde(getHuidigeSpelRonde().getActiveDeelname()) == 31) {
			JOptionPane.showMessageDialog(mf,"U heef 31, een nieuwe SpelRonde word gestart.", "Error", JOptionPane.ERROR_MESSAGE);
			volgendeRonde();
		}
		getHuidigeSpelRonde().volgendeSpeler();
		updateUI();
	}

	private void controleerPunten()
	{
		TreeSet<Integer> overblijvendeSpelerIds	= new TreeSet<Integer>();
		for (int spelerId : deelnemendeSpelerIds) {
			int aantalFiches = PlayersService.getInstance().geefSpelerDetails(spelerId).geefFiches();
			if (aantalFiches > 0) {
				overblijvendeSpelerIds.add(spelerId);
			}
		}
		if(overblijvendeSpelerIds.size() == 1) {
			String winnaar = PlayersService.getInstance().geefSpelerDetails(overblijvendeSpelerIds.first()).geefNaam();
			JOptionPane.showMessageDialog(mf, "Speler " + winnaar + " heeft gewonnen!", "Einde Spel!", JOptionPane.WARNING_MESSAGE);
			MainFrame frame = (MainFrame) mf;
			frame.dispose();
			new MainFrame(1);
			new ScoreOverzichtFrame(spelRondes);
		}
		deelnemendeSpelerIds = overblijvendeSpelerIds;
	}

	public void volgendeRonde()
	{
		nieuweSpelRonde();
		SpelRonde sr = (SpelRonde) spelRondes.lastElement();
		sr.setSpeler(gewonnenSpeler);
		JOptionPane.showMessageDialog(mf, "Nieuwe Ronde!", "Melding", JOptionPane.INFORMATION_MESSAGE);
		if(mf instanceof MainFrame) {
			MainFrame frame = (MainFrame) mf;
			frame.newRound();
		}
	}

	public void pas() {
		SpelRonde sp = (SpelRonde) spelRondes.lastElement();
		sp.setBeurtenTeGaan();
		sp.volgendeSpeler();
		updateUI();
	}

	public void doorSchuiven() {
		SpelRonde sp = getHuidigeSpelRonde();
		if (controleerWaarde(getHuidigeSpelRonde().getActiveDeelname()) == 31) {
			JOptionPane.showMessageDialog(mf,"U heef 31, een nieuwe SpelRonde word gestart.", "Error", JOptionPane.ERROR_MESSAGE);
			volgendeRonde();
		}
		else sp.volgendeSpeler();
		updateUI();
	}

	private void nieuweSpelRonde() {
		controleerPunten();
		SpelRonde sp = (SpelRonde)spelRondes.lastElement();
		Vector<?> deelnemers = sp.geefDeelnames();
		// eindScore per deelname wordt geset
		for (Iterator<?> i = deelnemers.iterator(); i.hasNext();) {
			Deelname dn = (Deelname) i.next();
			dn.geefEindScore();
		}
		spelRondes.add(new SpelRonde(this, tafel, deelnemendeSpelerIds));
		// winnende speler van afgelopen ronde wordt opgehaald
		int waarde = 0;
		int spelerId = 0;
		for (Iterator<?> i = deelnemers.iterator(); i.hasNext();) {			
			Deelname d1 = (Deelname)i.next();
			System.out.println(controleerWaarde(d1));
			if (controleerWaarde(d1) > waarde) {
				waarde = controleerWaarde(d1);
				spelerId = d1.getSpeler();
			}			
		}
		setGewonnenSpeler(spelerId);		
		getHuidigeSpelRonde().setSpeler(gewonnenSpeler);
	}

	private void setGewonnenSpeler(int s) {
	  this.gewonnenSpeler = s;
    }

    public Tafel getTafel() {
    	return tafel;
    }

    public SpelRonde getHuidigeSpelRonde() {
    	SpelRonde sp = (SpelRonde) spelRondes.lastElement();
    	return sp;
    }

    public Vector<?> getTotalScore(){
    	return new Vector<Object>();
    }

	public int controleerWaarde(Deelname dn)
	{
		Vector<KaartDTO> v = dn.getKaarten();
		Iterator<KaartDTO> i = v.iterator();
		KaartDTO k1 = i.next();
		KaartDTO k2 = i.next();
		KaartDTO k3 = i.next();
		int waarde = 0;
		if ((k1.geefSymbool().equals(k2.geefSymbool())) && (k1.geefSymbool().equals(k3.geefSymbool()))) {
			waarde = (k1.geefWaarde() + k2.geefWaarde() + k3.geefWaarde());
		}
		else if( (k1.geefSymbool().equals(k2.geefSymbool())) && !(k1.geefSymbool().equals(k3.geefSymbool()))) {
			waarde = (k1.geefWaarde() + (k2.geefWaarde()));
		}
		else if((k1.geefSymbool().equals(k3.geefSymbool())) && !(k1.geefSymbool().equals(k2.geefSymbool()))) {
			waarde = k1.geefWaarde() + k3.geefWaarde();
		}
		else if((k2.geefSymbool().equals(k3.geefSymbool())) && !(k2.geefSymbool().equals(k1.geefSymbool()))) {
			waarde = k2.geefWaarde() + k3.geefWaarde();
		}
		else {
			waarde = k1.geefWaarde();
			if (k2.geefWaarde() > waarde) waarde = k2.geefWaarde();
			if (k3.geefWaarde() > waarde) waarde = k3.geefWaarde();
		}
		return waarde;
	}
}