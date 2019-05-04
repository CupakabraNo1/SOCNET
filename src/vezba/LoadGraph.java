package vezba;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;




public class LoadGraph {
	private static UndirectedSparseGraph<Integer, String> graph;
	
	public LoadGraph() {
		try {
			graph = new UndirectedSparseGraph<Integer, String>();
			makeVertex();
			makeLinks();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//privremeno
	public static void main(String[] args) {
		
		System.out.println("br cvorova: " + graph.getVertexCount());
		System.out.println("br linkova: " + graph.getEdgeCount());
	}
	//privremeno...
	static {
		try {
			graph = new UndirectedSparseGraph<Integer, String>();
			makeVertex();
			makeLinks();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void makeVertex() throws IOException {
		Set<Integer> nodes = getVertex();
		Iterator<Integer> it = nodes.iterator();
		while(it.hasNext()) {
			graph.addVertex(it.next());
		}
	}
	
	private static void makeLinks() throws IOException {
		Files.lines(Paths.get("src/CA-GrQc.txt"))
				.forEach(x -> graph.addEdge(x, Integer.parseInt(x.split("\\t")[0]), Integer.parseInt(x.split("\\t")[1])));
	}
	
	private static Stream<String> loadData() throws IOException {
		return Files.lines(Paths.get("src/CA-GrQc.txt"))
				.flatMap(x -> Arrays.asList(x.split("\\t")).stream());
	}
	
	private static Set<Integer> getVertex() throws IOException {
		return loadData()
				.map(x -> Integer.parseInt(x)).collect(Collectors.toSet());
	}

	public static UndirectedSparseGraph<Integer, String> getGraph() {
		return graph;
	}
}
