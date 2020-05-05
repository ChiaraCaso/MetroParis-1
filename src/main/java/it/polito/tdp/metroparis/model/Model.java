package it.polito.tdp.metroparis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	private Graph<Fermata, DefaultEdge> graph;
	private List<Fermata> fermate;
	private Map <Integer, Fermata> fermateIdMap;
	
	public Model() {
		this.graph = new SimpleDirectedGraph<Fermata, DefaultEdge>(DefaultEdge.class);
		
		MetroDAO dao = new MetroDAO();
		
		//CREAZIONE DEI VERTICI
		this.fermate = dao.getAllFermate();
		
		//mi permette di passare dagli id alle fermate perchè il tempo di accesso così è costante o(1)
		this.fermateIdMap = new HashMap<Integer, Fermata>();
		for( Fermata f: this.fermate) {
			fermateIdMap.put(f.getIdFermata(), f);
		}
		
		Graphs.addAllVertices(this.graph, this.fermate);
		
		//System.out.println(this.graph);
		
		//CREAZIONE DEGLI ARCHI -- metodo 1 -> coppie di vertici (PUO' ESSERE LENTO)
	/*	for(Fermata fp : this.fermate) {
			for(Fermata fa : this.fermate) {
				if(dao.fermateConnesse(fp, fa)) {
					this.graph.addEdge(fp, fa);
				}
			}
		}
	*/	
		//CREAZIONE DEGLI ARCHI -- metodo 2 -> da un vertice trova tutti i connessi (PREFERIBILE RISPETTO AL METODO 1 SE IL GRADO MEDIO DEI VERTICI E' BASSO)
		
	/*	for (Fermata fp: fermate) {
			List<Fermata> connesse = dao.fermateSuccessive(fp, fermateIdMap);
			
			for(Fermata fa : connesse) {
				this.graph.addEdge(fp, fa);
			}
		}
	*/	
		//CREAZIONE DEGLI ARCHI -- metodo 3 -> chiedo al db l'elenco degli archi (TUTTA LA COMPLESSITA' E' SUL DB -> E' IL PIU' VELOCE SE IL DB HA LE INFORMAZIONI ORGANIZZATE BENE ED E' RAPIDO  )
		
		List <CoppiaFermate> coppie = dao.coppieFermate(fermateIdMap);
		for(CoppiaFermate c : coppie) {
			this.graph.addEdge(c.getFp(), c.getFa());
		}
		
		
		
		//System.out.println(this.graph);
		System.out.format("Grafo caricato con %d vertici e %d archi", this.graph.vertexSet().size(), this.graph.edgeSet().size());
		
	}
	
	
	
	public static void main(String args[]) {
		Model m = new Model();
	}
}
