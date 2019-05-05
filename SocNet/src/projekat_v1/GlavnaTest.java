package projekat_v1;

import java.time.Duration;
import java.time.Instant;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import projekat_v1.Metrics.Type;

public class GlavnaTest {

	public static void main(String[] args) {

		Instant bigStart = Instant.now();
		
		UndirectedSparseGraph<Integer, String> graph = LoadGraph.getGraph();
		
		System.out.println("\nPriprema za funkcionalnosti:\n");
		DetectComponentsDFS.setGraph(graph);
		DetectComponentsDFS.getSizeOfGiantComp();
		System.out.println("Ukupan broj cvorova: " + graph.getVertexCount());
		LoadGraph.getModifiedGraph(graph, DetectComponentsDFS.getGiantComponent());
		System.out.printf("Gigantsku komponentu cini %d cvorova.\n", graph.getVertexCount());

		System.out.println("\n1. funkcionalnost:\n");
		Instant timeStart = Instant.now();
		Metrics.calculateCentrality(Type.CLOSENESS, graph); //degree
		Instant timeEnd = Instant.now();
		System.out.printf("Pirsonov koeficijent: %.3f\n", Metrics.getPearsons());
		System.out.printf("Spermanov koeficijent: %.3f\n", Metrics.getSpearmans());
		Duration duration = Duration.between(timeStart, timeEnd);
		long ms = duration.toMillis();
		System.out.printf("Vreme racunanja metrike: %dms (~%dsec)\n", ms, ms/1000);

		System.out.println("\n2. funkcionalnost:\n");
		System.out.println("...");
		
		System.out.println("\n3. funkcionalnost:\n");
//		TenPercent<Integer, String> tp = new TenPercent<>(graph);
//		tp.calculatePercentage();
		
		Instant bigEnd = Instant.now();
		Duration duration2 = Duration.between(bigStart, bigEnd);
		long ms2 = duration2.toMillis();
		long min2 = duration2.toMinutes();
		System.out.printf("Vreme izvrsavanja programa: %dsec (~%dmin)\n", ms2/1000, min2);
	}
}
