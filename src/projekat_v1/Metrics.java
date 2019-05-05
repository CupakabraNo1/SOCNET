package projekat_v1;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class Metrics {
	
	private static PearsonsCorrelation pearsons;
	private static SpearmansCorrelation spearmans;
	
	private static BetweennessCentrality<Integer, String> bc;
	private static ClosenessCentrality<Integer, String> cc;
	private static EigenvectorCentrality<Integer, String> ec;
	
	private static double[] arr1;;
	private static double[] arr2;
	private static int counter;

	public enum Type {
		DEGREE, BETWEENNESS, CLOSENESS, EIGENVECTOR;
	}
	//										        , MyUndirectedSparseGraph<My...V, My...E> g
	public static void calculateCentrality(Type type, UndirectedSparseGraph<Integer, String> g) {
		arr1 = new double[g.getEdgeCount() * 2];
		arr2 = new double[g.getEdgeCount() * 2];
		counter = 0;
		calculateMetric(type, g);
	}
	
	private static void calculateMetric(Type type, UndirectedSparseGraph<Integer, String> g) {
		switch (type) {
		case DEGREE:
			System.out.println("Racunanje 'degree' centralnosti...");
			//dal ovo moze u neku metodu da se izdvoji...
			for(String s : g.getEdges()) {
				Pair<Integer> pair = g.getEndpoints(s);
				Integer v1 = pair.getFirst();
				Integer v2 = pair.getSecond();
				arr1[counter] = g.degree(v1);
				arr2[counter++] = g.degree(v2);
				arr1[counter] = g.degree(v2);
				arr2[counter++] = g.degree(v1);
			}
			break;
		case BETWEENNESS:
			System.out.println("Racunanje 'betweenness' centralnosti...");
			bc = new BetweennessCentrality<Integer, String>(g);
			for(String s : g.getEdges()) {
				Pair<Integer> pair = g.getEndpoints(s);
				Integer v1 = pair.getFirst();
				Integer v2 = pair.getSecond();
				arr1[counter] = bc.getVertexScore(v1);
				arr2[counter++] = bc.getVertexScore(v2);
				arr1[counter] = bc.getVertexScore(v2);
				arr2[counter++] = bc.getVertexScore(v1);
			}
			break;
		case CLOSENESS:
			System.out.println("Racunanje 'closeness' centralnosti...");
			cc = new ClosenessCentrality<Integer, String>(g);
			System.out.println(g.getEdgeCount());
			
			int br = 0;
			
//			List<String> list = g.getEdges().stream()
//				.collect(Collectors.toList());

			for(String s : g.getEdges()) {
				Pair<Integer> pair = g.getEndpoints(s);
				Integer v1 = pair.getFirst();
				Integer v2 = pair.getSecond();
				arr1[counter] = cc.getVertexScore(v1);
				System.out.print("-");
				arr2[counter++] = cc.getVertexScore(v2);
				System.out.print("-");
				arr1[counter] = cc.getVertexScore(v2);
				System.out.print("-");
				arr2[counter++] = cc.getVertexScore(v1);
				System.out.println(br++);
			}
			System.out.println("Ovde");
			break;
		case EIGENVECTOR:
			System.out.println("Racunanje 'eigenvector' centralnosti...");
			ec = new EigenvectorCentrality<Integer, String>(g);
			for(String s : g.getEdges()) {
				Pair<Integer> pair = g.getEndpoints(s);
				Integer v1 = pair.getFirst();
				Integer v2 = pair.getSecond();
				arr1[counter] = ec.getVertexScore(v1);
				arr2[counter++] = ec.getVertexScore(v2);
				arr1[counter] = ec.getVertexScore(v2);
				arr2[counter++] = ec.getVertexScore(v1);
			}
			break;
		default:
			break;
		}
	}
	
	public static double getPearsons() {
		pearsons = new PearsonsCorrelation();
		return pearsons.correlation(arr1, arr2);
	}

	public static double getSpearmans() {
		spearmans = new SpearmansCorrelation();
		return spearmans.correlation(arr1, arr2);
	}
}
