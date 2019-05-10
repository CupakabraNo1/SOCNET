package calculations;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import data.Metrics;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class Decomposition<V,E>{
	
	private UndirectedSparseGraph<V, E> graph;
	private MetricData<V, E> data;
	
	
	public Decomposition(MetricData<V, E> data) throws IOException {
		this.data=data;
		this.graph=data.getGraph();
		decomposition();
	}
	
	public void decomposition() throws IOException {
		System.out.println("Processing decomposition...");
		
		List<V>orderD=formOrder(Metrics.DEGREE);
		List<V>orderB=formOrder(Metrics.BETWEENNESS);
		List<V>orderC=formOrder(Metrics.CLOSENESS);
		List<V>orderE=formOrder(Metrics.EIGEN);
		System.gc();
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
		System.out.println("Eigenvector decomposition calculated.");
		System.gc();
		
		File fajl=new File("data.csv");
		PrintWriter writer=new PrintWriter(fajl);
		System.out.println("Writing in file...");
		writer.append("F,SD,SB,SC,SE\n");
		Map<Integer,List<Double>> map=new TreeMap<>();
		for(int i=0;i<orderD.size()-1;i++) {
			Double[] array={degreeDecomp.get(i+1),betweennessDecomp.get(i+1),closenessDecomp.get(i+1),eigenDecomp.get(i+1)};
			map.put(i+1,Arrays.asList(array));
		}
		
		System.out.println("Writing ended.");
		map.entrySet().stream().map(x->x.getKey()+","+format(x.getValue())).forEach(x->writer.println(x));
		writer.close();
		
		System.out.println("Decomposition completed. Your data can be found in file \"data.csv\".");
	}
	
	private static String format(List<Double> l) {
		return l.stream().map(x->String.format("%.2f",x)).collect(Collectors.joining(","));
	}
	
	private TreeMap<Integer,Double> decomp(UndirectedSparseGraph<V,E> dummy, List<V> order){
		TreeMap<Integer,Double> map=new TreeMap<>();
		int position=0;
		double perc=0.0;
		Giant<V, E> giga=null;
		for(V v:order) {
			position=order.indexOf(v);
			dummy.removeVertex(v);
		    giga=new Giant<>(dummy);
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

	public UndirectedSparseGraph<V, E> collect(){
		UndirectedSparseGraph<V, E> dummy=data.getGraph();
		int number=(int)Math.round((graph.getEdgeCount()*10.0)/100);
		List<E> list=graph.getEdges().stream().sorted(Comparator.comparing(x->-data.getBcL(x))).collect(Collectors.toList());
		for(int i=0; i< graph.getEdgeCount(); i++) {
			if(i < number-1) continue;
			dummy.removeEdge(list.get(i));
		}
		return dummy;
	}
	
}

