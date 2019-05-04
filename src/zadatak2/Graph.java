package zadatak2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class Graph {
	
	private static UndirectedSparseGraph<Integer, String> graph;

	public Graph() throws IOException {
		graph=new UndirectedSparseGraph<Integer, String>();
		loadData();
	}
	
	public UndirectedSparseGraph<Integer, String> getGraph(){
		return graph;
	}
	
	public void setGraph(UndirectedSparseGraph<Integer, String> g) {
		graph=g;
	}
	
	private static void loadData() throws IOException {
		
		//making vertices for graph
		Set<String> vertices=Loader.load()
				.flatMap(x-> Arrays.asList(x.split("\\t")).stream())
				.collect(Collectors.toSet());
		
		vertices.stream().forEach(x-> graph.addVertex(Integer.parseInt(x)));
		
		//making links beetween vertices
		Loader.load().forEach(x->{
			String [] splited=x.split("\\t");
			graph.addEdge(x ,Integer.parseInt(splited[0].trim()), Integer.parseInt(splited[1].trim()));
		});
		
	}
	
	public Graph filter(Set<Integer> giga) throws IOException {
		Graph g=new Graph();
		UndirectedSparseGraph<Integer, String> usg=new UndirectedSparseGraph<Integer, String>();
		for(Integer v:giga) {
			usg.addVertex(v);
		}
		
		for(String s:g.getGraph().getEdges()) {
			Pair<Integer> pair=g.getGraph().getEndpoints(s);
			if(s!=null && usg.getVertices().contains(pair.getFirst()) && usg.getVertices().contains(pair.getSecond()))
				usg.addEdge(s,pair.getFirst() ,pair.getSecond());
		}
		g.setGraph(usg);
		return g;
	}
	
//	public static Graph clone(Graph g) {
//		Graph clone=new Graph();
//		UndirectedSparseGraph<Integer, String> usg=new UndirectedSparseGraph<Integer, String>();
//		g.getGraph().getVertices().stream()
//			.forEach(x->usg.addVertex(x));
//		g.getGraph().getEdges().stream()
//			.map(x->g.getGraph().getEndpoints(x))
//			
//	}
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		graph.getEdges().stream().forEach(x->builder.append(x+"\n"));
		builder.append("Number of vertices is= "+graph.getVertexCount());
		return builder.toString();
	}
	
	public static String printGraph(Graph g) {
		StringBuilder builder=new StringBuilder();
		g.getGraph().getEdges().stream().forEach(x->builder.append(x+"\n"));
		builder.append("Number of vertices is= "+g.getGraph().getVertexCount());
		return builder.toString();
	}
}
