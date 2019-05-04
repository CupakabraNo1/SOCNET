package zadatak2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class Decomposition <V,E>{
	
	private UndirectedSparseGraph<V, E> graph;
	
	
	DegreeScorer<V> ds;
	BetweennessCentrality<V, E> bc;
	ClosenessCentrality<V, E> cc;
	EigenvectorCentrality<V, E> ec;
	
	public Decomposition(UndirectedSparseGraph<V, E> graph) throws IOException {
		this.graph=clone(graph);
		System.out.println("a");
		centralitis();
		System.out.println("b");
		
		decomposition();
	}

	public UndirectedSparseGraph<V,E> clone(UndirectedSparseGraph<V,E> forCloning){
		UndirectedSparseGraph<V,E> clone=new UndirectedSparseGraph<V,E>();
		for(V v:forCloning.getVertices()) {
			clone.addVertex(v);
		}
		
		for(E e:forCloning.getEdges()) {
			Pair<V> pair=forCloning.getEndpoints(e);
			clone.addEdge(e, pair.getFirst(), pair.getSecond());
		}
		return clone;	
	}

	private void centralitis() {
		ds=new DegreeScorer<V>(graph);
//		bc=new BetweennessCentrality<V,E>(graph);
//		cc=new ClosenessCentrality<V,E>(graph);
//		ec=new EigenvectorCentrality<V,E>(graph);
		
	}
	
	static class Data{
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
			graph.removeVertex(v);
			Gigant<V, E> gigant=new Gigant<V, E>(graph);
			perc=gigant.percent();
			data.sd=perc;
			graph.addVertex(v);
			
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
			return graph.getVertices().stream().sorted(Comparator.comparing(x->-ds.getVertexScore(x))).collect(Collectors.toList());
//		case BETWEENNESS:
//			return graph.getVertices().stream().sorted(Comparator.comparing(x->-bc.getVertexScore(x))).collect(Collectors.toList());
//		case CLOSENESS:
//			return graph.getVertices().stream().sorted(Comparator.comparing(x->-cc.getVertexScore(x))).collect(Collectors.toList());	
//		case EIGEN:
//			return graph.getVertices().stream().sorted(Comparator.comparing(x->-ec.getVertexScore(x))).collect(Collectors.toList());
		default:
			return null;
		}
//		
	}
	
	
	
}
