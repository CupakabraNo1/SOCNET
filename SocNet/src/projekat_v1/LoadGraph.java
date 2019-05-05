package projekat_v1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class LoadGraph {
	
	private static UndirectedSparseGraph<Integer, String> graph;
	
	public static UndirectedSparseGraph<Integer, String> getGraph() {
		return graph;
	}

	static {
		try {
			graph = new UndirectedSparseGraph<Integer, String>();
			makeVertices();
			makeLinks();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void makeVertices() throws IOException {
		Set<Integer> nodes = getVertices();
		Iterator<Integer> it = nodes.iterator();
		while(it.hasNext()) {
			graph.addVertex(it.next());
		}
	}

	private static Set<Integer> getVertices() throws IOException {
		return loadVertices()
				.map(x -> Integer.parseInt(x)).collect(Collectors.toSet());
	}

	private static Stream<String> loadVertices() throws IOException {
		return Files.lines(Paths.get("src/CA-GrQc.txt"))
				.flatMap(x -> Arrays.asList(x.split("\\t")).stream());
	}
	
	
	private static void makeLinks() throws IOException {
		Files.lines(Paths.get("src/CA-GrQc.txt"))
				.forEach(x -> graph.addEdge(
						x, 
						Integer.parseInt(x.split("\\t")[0]), 
						Integer.parseInt(x.split("\\t")[1])
					)); 
	}
	
	
	public static <T> void getModifiedGraph(UndirectedSparseGraph<T, ?> g, Set<? super T> component) {
		getModifiedGraphTMP(g, component);
	}
	private static <T> void getModifiedGraphTMP(UndirectedSparseGraph<T, ?> g, Set<? super T> component) {
		Set<T> tmp = new HashSet<>(g.getVertices());
		for (Iterator<T> it = tmp.iterator(); it.hasNext();) {
		    T node = it.next();
		    if(!component.contains(node)) {
				it.remove();
				g.removeVertex(node);
			}
		}
	}
	
	
	public static <V, E> UndirectedSparseGraph<V, E> getCopyOfGraph(UndirectedSparseGraph<V, E> g) {
		return getCopyOfGraphTMP(g);
	}
	private static <V, E> UndirectedSparseGraph<V, E> getCopyOfGraphTMP(UndirectedSparseGraph<V, E> g) {
		UndirectedSparseGraph<V, E> newGraph = new UndirectedSparseGraph<V, E>();
		for(V v : g.getVertices())
			newGraph.addVertex(v);
		for(E e : g.getEdges())
			newGraph.addEdge(e, g.getIncidentVertices(e));
		return newGraph;
	}
}