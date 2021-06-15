package it.polito.tdp.genes.model;

import java.util.*; 
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;

import it.polito.tdp.genes.db.GenesDao;

public class Model
{
	private GenesDao dao;
	private Map<String, Genes> vertici; 
	private Graph<Genes, DefaultWeightedEdge> grafo; 

	public Model()
	{
		this.dao = new GenesDao();
	}

	public void creaGrafo()
	{
		// ripulisco mappa e grafo
		this.vertici = new HashMap<>(); 
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); // 

		/// vertici 
		this.dao.getVertici(vertici); //riempio la mappa
		Graphs.addAllVertices(this.grafo, this.vertici.values()); 

		/// archi
		List<Adiacenza> adiacenze = new ArrayList<>(this.dao.getAdiacenze(vertici));
		for (Adiacenza a : adiacenze)
		{
			Genes g1 = a.getA1();
			Genes g2 = a.getA2();
			double peso = a.getPeso(); 

			if(g1.getChromosome()==g2.getChromosome())
				Graphs.addEdge(this.grafo, g1, g2, 2 * peso);
			else 
				Graphs.addEdge(this.grafo, g1, g2, peso);
		}
	}
	public int getNumVertici()
	{
		return this.grafo.vertexSet().size();
	}
	public int getNumArchi()
	{
		return this.grafo.edgeSet().size();
	}
	public Collection<Genes> getVertici()
	{
		List<Genes> vertici = new ArrayList<>(this.grafo.vertexSet()); 
		vertici.sort((v1,v2)->v1.getGeneId().compareTo(v2.getGeneId())); 
		return vertici;
	}
	public Collection<DefaultWeightedEdge> getArchi()
	{
		return this.grafo.edgeSet();
	}

	private List<Genes> getAdiacenti(Genes g)
	{
		List<Genes> vicini = Graphs.neighborListOf(this.grafo, g);
		vicini.sort((v1,v2)-> - Double.compare(grafo.getEdgeWeight(grafo.getEdge(v1, g)), grafo.getEdgeWeight(grafo.getEdge(v2, g))));
		return vicini; 
	}

	public String stampaAdiacenti(Genes g)
	{
		String s = ""; 
		for (Genes v : this.getAdiacenti(g))
		{
			s += String.format(" - %s [%.2f]\n", v, grafo.getEdgeWeight(grafo.getEdge(v, g)));
		}
		return s;
	}

	//SIMULAZIONE

	List<Genes> ingegneri ;
	final int PROB_CONTINUA = 30; 
	final int PROB_CAMBIO = 70; 

	public void simula(int numIng, Genes partenza)
	{
		this.ingegneri = new ArrayList<>(); 
		for(int i = 0; i < numIng; i++)
		{
			ingegneri.add(partenza); 
		}

		//simula
		for(int i = 0; i < 36; i++)
		{
			for(int gene = 0; gene < ingegneri.size(); gene++)
			{
				double prob = Math.random() * 100; 
				if(prob < PROB_CONTINUA) //continua
				{
					continue; 
				}
				else //cambia
				{
					Genes newG = null; 
					
					List<Genes> vicini = this.getAdiacenti(ingegneri.get(gene)); //adiancenti all'ultimo gene studiato dal ing
					Collections.reverse(vicini);
					
					double pesoTot = 0; 
					for(Genes v : vicini)
						pesoTot += grafo.getEdgeWeight(grafo.getEdge(v, ingegneri.get(gene)));
					
					double rand = Math.random(); 
					System.out.println("-> rand " +rand);
					for(Genes v : vicini)
					{
						double peso = grafo.getEdgeWeight(grafo.getEdge(v, ingegneri.get(gene))) / pesoTot;
						System.out.println("gene " + v + "peso" + peso);
						if(rand < peso)
						{
							System.out.println("-> -> scelto gene: " + v);
							newG = v; 
							break;
						}
					}
					if(newG == null)
					{
						newG = vicini.get(vicini.size()-1); 
						System.out.println("scelto gene: " + newG);
					}
					
					//cambio gene studiato 
					ingegneri.set(gene, newG); 
				}
			}
		}
		System.out.println(ingegneri);
	}
}
