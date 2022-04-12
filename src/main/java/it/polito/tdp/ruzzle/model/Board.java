package it.polito.tdp.ruzzle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.ruzzle.db.DizionarioDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Memorizza le lettere presenti nella scacchiera Ruzzle.
 * @author Fulvio
 *
 */
public class Board {
	private List<Pos> positions;
	private Map<Pos, StringProperty> cells;
	DizionarioDAO dao = new DizionarioDAO();

	private int size;

	/**
	 * Crea una nuova scacchiera della dimensione specificata
	 * @param size
	 */
	public Board(int size) {
		this.size = size;

		//Definisco le "caselle" del gioco (e la forma del piano di gioco)
		this.positions = new ArrayList<>();
		for (int row = 0; row < this.size; row++) {
			for (int col = 0; col < this.size; col++) {
				this.positions.add(new Pos(row, col));
			}
		}

		//Definisco il contenuto delle caselle
		this.cells = new HashMap<>();

		//Ogni casella conterrà una String Property, inizialmente vuota, per contenere il proprio carattere  
		for (Pos p : this.positions) {
			this.cells.put(p, new SimpleStringProperty());
		}
	}
	
	/**
	 * Fornisce la {@link StringProperty} corrispondente alla {@link Pos} specificata. <p>
	 * 
	 * Può essere usata per sapere che lettera è presente
	 * (es. {@code getCellValueProperty(p).get()}) oppure per fare un binding della proprietà stessa sulla mappa visuale.
	 * @param p
	 * @return
	 */
	public StringProperty getCellValueProperty(Pos p) {
		return this.cells.get(p) ;
	}

	/**
	 * Restituisce la lista di oggetti {@link  Pos} che corrispondono alle posizioni lecite sulla scacchiera. Gli elementi sono ordinati per righe.
	 * @return
	 */
	public List<Pos> getPositions() {
		return positions;
	}

	/**
	 * Crea una nuova scacchiera generando tutte lettere casuali
	 */
	
	public void reset() {
		List<String> lista=dao.listParola();
		int numeroParole=lista.size()-1;
		for(Pos p: this.positions) {
			/*
			int random = (int)(Math.random()*26) ;
			String letter = Character.toString((char)('A'+random)) ;
			
			
			OPPURE
			Ho implementato un metodo poco preciso per avere delle lettere
			che abbiano una probabilità maggiore di comparire in una parola,
			è meglio del metodo casuale ma non è il meglio che si può avere 
			*/
			int indiceParola=(int)(Math.random()*numeroParole);
			String parola= lista.get(indiceParola);
			int lunghezzaParola= parola.length()-1;
			int posCarattere=(int) Math.random()*lunghezzaParola;
			String letter=parola.substring(posCarattere, posCarattere+1).toUpperCase();
			
			//grazie al "binding" fatto in FXMLController, la "set" modifica direttamente il testo del botone collegato alla posizione corrente
			this.cells.get(p).set(letter); 
		}
	}
	
	/**
	 * Data una posizione, restituisce tutte le posizioni adiacenti
	 * @param p
	 * @return
	 */
	public List<Pos> getAdjacencies(Pos p) {
		List<Pos> result = new ArrayList<>() ;
		
		for(int r = -1; r<=1; r++) {
			for(int c = -1; c<=1; c++) {
				// tutte le 9 posizioni nell'intorno della cella				
				if(r!=0 || c!=0) { // escludo la cella stessa (offset 0,0)
					Pos adj = new Pos(p.getRow()+r, p.getCol()+c) ;
					//controllo che gli indici non siano fuori dalla griglia
					if(positions.contains(adj)) {
						result.add(adj) ;
					}
				}
			}
		}
		
		return result ;
	}


	
}
