package zadatak2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class Decomposition <V,E>{
	
	private UndirectedSparseGraph<V, E> graph;
	private MetricData<V, E> data;
	
	
	public Decomposition(UndirectedSparseGraph<V, E> graph) throws IOException {
		this.graph=graph;
		data=new MetricData(graph);
		System.out.println("a");
//		decomposition();
	}
	
	class Data{
		int row;
		double sd,sb,sc,se;
		
		public Data(int row) {
			this.row=row;
			sd=0.0;
			sb=0.0;
			sc=0.0;
			se=0.0;
		}
		
		public String format() {
			return String.format("%d,%2.3f%%,%2.3f%%,%2.3f%%,%2.3f%%%n", row,sd,sb,sc,se);
		}
	}
	
	public void decomposition() throws IOException {
		UndirectedSparseGraph<V, E> dummy=data.getGraph();
		Collection<E> neighbours=null;
		
		List<V>orderD=new LinkedList<V>();
		List<V>orderB=new LinkedList<V>();
		List<V>orderC=new LinkedList<V>();
		List<V>orderE=new LinkedList<V>();
		
		orderD=formOrder(Metrics.DEGREE);
		orderB=formOrder(Metrics.BETWEENNESS);
		orderC=formOrder(Metrics.CLOSENESS);
		orderE=formOrder(Metrics.EIGEN);
		
		BufferedWriter writer=new BufferedWriter(new FileWriter("data.csv"));
		writer.append("F,DS,BS,CS,ES\n");
		for(int index=0;index<graph.getVertexCount();index++) {
			Data data=new Data(index);
			double perc=0.0;
			
			//by degree
			V v=orderD.get(index);
			neighbours.addAll(dummy.getIncidentEdges(v));
			dummy.removeVertex(v);
			
			Gigant<V, E> gigant=new Gigant<V, E>(graph);
			perc=gigant.percent();
			data.sd=perc;
			dummy.addVertex(v);
			
			
			
			//by betweenness
			v=orderB.get(index);
			graph.removeVertex(v);
			gigant=new Gigant<V, E>(graph);
			perc=gigant.percent();
			data.sd=perc;
			
			//by closeness
			graph.addVertex(v);
			v=orderC.get(index);
			graph.removeVertex(v);
			gigant=new Gigant<V, E>(graph);
			perc=gigant.percent();
			data.sd=perc;
			graph.addVertex(v);
			
			//by eigen
			v=orderE.get(index);
			graph.removeVertex(v);
			gigant=new Gigant<V, E>(graph);
			perc=gigant.percent();
			data.sd=perc;
			graph.addVertex(v);
			
			writer.append(data.format());
		}
		System.out.println("Decomposition completed your data can be found in file \"data.csv\"");
	}

	private List<V> formOrder(Metrics m) {
		switch (m) {
		case DEGREE: 
			return graph.getVertices().stream().sorted(Comparator.comparing(x->-data.getDs(x))).collect(Collectors.toList());	
		case BETWEENNESS:
			return graph.getVertices().stream().sorted(Comparator.comparing(x->-data.getBc(x))).collect(Collectors.toList());
		case CLOSENESS:
			return graph.getVertices().stream().sorted(Comparator.comparing(x->-data.getCc(x))).collect(Collectors.toList());	
		case EIGEN:
			return graph.getVertices().stream().sorted(Comparator.comparing(x->-data.getEc(x))).collect(Collectors.toList());
		default:
			return null;
		}		
	}
	
	public Set<V> collect(){
		int number=(graph.getVertexCount()*10)/100;
		List<V> list=formOrder(Metrics.BETWEENNESS);
		Set<V> output=new HashSet<V>();
		for(V v:list)
			if(output.size()<=number) output.add(v);
		return output;
	}
	
}

