package zadatak2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class Gigant <V,E> {
	
	private UndirectedSparseGraph<V, E> graph;
	private Set<Set<V>> components;
	private Set<V> visited;
	private int graphSize;
	private Set<V> giga;
	
	public Gigant(UndirectedSparseGraph<V, E>graph){
		this.graph=graph;
		graphSize=graph.getVertexCount();
		visited=new HashSet<>();
		components=new HashSet<>();
		fromGraph();
		giga=gigant();
	}
	
	private void fromGraph () {
		for(V v:graph.getVertices()) {
			if (!visited.contains(v)) {
				components.add(findComponent(v));
			}
		}
	}
	
	private Set<V> findComponent(V vertex) {
		Set<V> component=new HashSet<>();
		component.add(vertex);
		dfs(component, vertex);
		return component;
	}
	
	private void dfs(Set<V> comp, V vert) {
		Collection<V> linked=graph.getNeighbors(vert);
		for(V v:linked) {
			if(!comp.contains(v)) {
				comp.add(v);
				visited.add(v);
				dfs(comp,v);
			}
		}
	}
	
	public Set<V> gigant() {
		Set<V> giga=null;
		for(Set<V> s:components) {
			if(giga==null || s.size()>giga.size() )
				giga=s;
		}
		
		if(giga!=null) {
			return giga;
		}
		
		return null;		 
	}
	
	public double percent() {
		double perc=0.0;
		perc=(100*giga.size())/graphSize;
		return perc;
	}
	
	
}
