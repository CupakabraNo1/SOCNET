package zadatak2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.collections15.map.HashedMap;

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
		System.out.println("Metric data calculated, procesing decomposition...");
		decomposition();
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
			return String.format("%d,%2.3f%%,%2.3f%%,%2.3f%%,%2.3f%%%n",row,sd,sb,sc,se);
		}
	}
	
	public void decomposition() throws IOException {
		
		List<V>orderD=formOrder(Metrics.DEGREE);
		List<V>orderB=formOrder(Metrics.BETWEENNESS);
		List<V>orderC=formOrder(Metrics.CLOSENESS);
		List<V>orderE=formOrder(Metrics.EIGEN);
		
		UndirectedSparseGraph<V, E> dummy=data.getGraph();
		Map<Integer,Double> degreeDecomp=decomp(dummy,orderD);
		System.out.println("Degree decomposition calculated.");
		dummy=data.getGraph();
		Map<Integer,Double> betweennessDecomp=decomp(dummy,orderB);
		System.out.println("Betweenness decomposition calculated.");
		dummy=data.getGraph();
		Map<Integer,Double> closenessDecomp=decomp(dummy,orderC);
		System.out.println("Closeness decomposition calculated.");
		dummy=data.getGraph();
		Map<Integer,Double> eigenDecomp=decomp(dummy,orderE);
		System.out.println("Eigen decomposition calculated.");
		
//		BufferedWriter writer=new BufferedWriter(new FileWriter("data.csv"));
//		writer.append("F,DS,BS,CS,ES\n");
		System.out.println("F,DS,BS,CS,ES/n");
		for(int i=0; i<orderD.size(); i++) {
			Data data=new Data(i+1);
			data.sd=degreeDecomp.get(i+1);
			data.sb=betweennessDecomp.get(i+1);
			data.sc=closenessDecomp.get(i+1);
			data.se=eigenDecomp.get(i+1);
//			writer.append(data.format());
//			System.out.println(i+","+data.sd+"%,"+data.sb+"%,"+data.sc+"%,"+data.se+"%");
			System.out.println(data.format());
		}
		System.out.println("Decomposition completed your data can be found in file \"data.csv\"");
	}
	
	private TreeMap<Integer,Double> decomp(UndirectedSparseGraph<V,E> dummy, List<V> order){
		TreeMap<Integer,Double> map=new TreeMap();
		int position=0;
		double perc=0.0;
		Gigant<V, E> giga=null;
		for(V v:order) {
			position=order.indexOf(v);
			dummy.removeVertex(v);
		    giga=new Gigant(dummy);
			perc=giga.percent();
			map.put(position+1,perc);
		}
		return map;
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

