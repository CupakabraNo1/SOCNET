package zadatak2;

import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class MetricData<V,E> {
	
	private UndirectedSparseGraph<V, E> graph;
	private DegreeScorer<V> ds;
	private BetweennessCentrality<V, E> bc;
	private ClosenessCentrality<V, E> cc;
	private EigenvectorCentrality<V, E> ec;
	
	public MetricData(UndirectedSparseGraph<V, E>graph) {
		this.graph=clone(graph);
		calculate();
	}
	
	private void calculate() {
		long bT=System.currentTimeMillis();
		ds=degree(graph);
		long eT=System.currentTimeMillis();
		System.out.println("Time of calculating degree="+ (eT - bT)/1000 + "seconds");
		bT=System.currentTimeMillis();
		bc=betweenness(graph);
		eT=System.currentTimeMillis();
		System.out.println("Time of calculating betweennes="+ (eT - bT)/1000 + "seconds");
		bT=System.currentTimeMillis();
		cc=closeness(graph);
		eT=System.currentTimeMillis();
		System.out.println("Time of calculating closeness="+ (eT - bT)/1000 + "seconds");
		bT=System.currentTimeMillis();
		ec=eigen(graph);	
		eT=System.currentTimeMillis();
		System.out.println("Time of calculating eigen="+ (eT - bT)/1000 + "seconds");
	}
	
	public UndirectedSparseGraph<V, E> getGraph(){
		return clone(graph);
	}
	public double getDs(V v) {
		return ds.getVertexScore(v);
	}
	
	public double getBc(V v) {
		return bc.getVertexScore(v);
	}
	
	public double getCc(V v) {
		return cc.getVertexScore(v);
	}
	
	public double getEc(V v) {
		return ec.getVertexScore(v);
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
	
	public static <V, E> DegreeScorer<V> degree(UndirectedSparseGraph<V, E> graph){
		return new DegreeScorer<V>(graph);
	}
	
	public static <V,E> BetweennessCentrality<V, E> betweenness(UndirectedSparseGraph<V, E> graph){
		return new BetweennessCentrality<V, E>(graph);
	}
	
	public static <V,E> ClosenessCentrality<V, E> closeness(UndirectedSparseGraph<V, E> graph){
		return new ClosenessCentrality<V, E>(graph);
	}
	
	public static <V,E> EigenvectorCentrality<V, E> eigen (UndirectedSparseGraph<V, E> graph){
		EigenvectorCentrality<V, E> eigen=new EigenvectorCentrality<V, E>(graph);
		eigen.evaluate();
		return eigen;
	}
	
}
