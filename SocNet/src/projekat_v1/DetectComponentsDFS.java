package projekat_v1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class DetectComponentsDFS {

	private static UndirectedSparseGraph<Object, Object> graph;
	private static Set<Object> giantComponent;

	@SuppressWarnings("unchecked")
	public static void setGraph(UndirectedSparseGraph<?, ?> graph) {
		DetectComponentsDFS.graph = (UndirectedSparseGraph<Object, Object>) graph;
		detectComp();
		getGiantComponent();
	}
	
	private static Set<Object> visited;
	private static Set<Set<Object>> components;
	
	
	private static void detectComp() {
		visited = new HashSet<>();
		components = new HashSet<>();
		for(Object node : graph.getVertices()) {
			if(!visited.contains(node)) {
				components.add(detectComp(node));
			}
		}
	}
	
	private static Set<Object> detectComp(Object node) {
		Set<Object> component = new HashSet<>();
		component.add(node);
		visited.add(node);
		dfs(node, component);
		return component;
	}
	
	private static void dfs(Object node, Set<Object> component) {
		Iterator<Object> it = graph.getNeighbors(node).iterator();
		while(it.hasNext()) {
			Object n = it.next();
			if(!visited.contains(n)) {
				component.add(n);
				visited.add(n);
				dfs(n, component);
			}
		}
	}
	
	
	public static Set<Set<Object>> getComponents() {
		return getComponentsTMP();
	}
	private static Set<Set<Object>> getComponentsTMP() {
		return components;
	}
	
	
	public static Set<Object> getGiantComponent() {
		return getGiantComponentTMP();
	}
	@SuppressWarnings("unchecked")
	private static Set<Object> getGiantComponentTMP() {
		Set<Object> giantComp = null;
		Iterator<?> it = DetectComponentsDFS.getComponents().iterator();
		while(it.hasNext()) {
			if(giantComp == null) giantComp = (Set<Object>) it.next();
			else {
				Set<Object> tmp = (Set<Object>) it.next();
				if(tmp.size() > giantComp.size()) giantComp = tmp;
			}
		}
		giantComponent = giantComp;
		return giantComp;
	}
	
	
	public static void getSizeOfGiantComp() {
		System.out.printf("Gigantska komponenta sadrzi %.2f%% cvorova celog grafa\n", (100.0 * giantComponent.size() / graph.getVertexCount()));
	}
}
