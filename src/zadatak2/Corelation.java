package zadatak2;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class Corelation <V,E> {
	
	private UndirectedSparseGraph<V, E> graph;
	private double pearsons;
	private double spearmans;
	
	DegreeScorer<V> ds;
	BetweennessCentrality<V, E> bc;
	ClosenessCentrality<V, E> cc;
	EigenvectorCentrality<V, E> ec;
	
	public double getPearsons() {
		return pearsons;
	}
	
	public double getSpearmans() {
		return spearmans;
	}
	
	public Corelation(UndirectedSparseGraph<V, E> graph, Metrics m) {
		this.graph=graph;
		corellations(m);
	}

	private void corellations(Metrics m) {
		metrics(m);
		double[] x = new double[graph.getEdgeCount() * 2];
		double[] y = new double[graph.getEdgeCount() * 2];
		int counter = 0;
		for (E e:graph.getEdges()) {
			Pair<V> pair=graph.getEndpoints(e);
			V v1 = pair.getFirst();
			V v2 = pair.getSecond();
			
			x[counter] = metric(v1,m);
			y[counter] = metric(v2,m);
			++counter;
			
			x[counter] = metric(v2,m);
			y[counter] = metric(v1,m);
			++counter;
		}	
		
		PearsonsCorrelation pc=new PearsonsCorrelation();
		pearsons=pc.correlation(x, y);
		
		SpearmansCorrelation sc=new SpearmansCorrelation();
		spearmans=sc.correlation(x, y);
	}
	
	private void metrics(Metrics m) {
		switch (m) {
		case DEGREE: 
			ds=new DegreeScorer<V>(graph);
		case BETWEENNESS:
			bc=new BetweennessCentrality<V,E>(graph);
		case CLOSENESS:
			cc=new ClosenessCentrality<V,E>(graph);
		case EIGEN:
			ec=new EigenvectorCentrality<V, E>(graph);
		default:
			return;
		}
		
	}
	
	private double metric(V v,Metrics m) {
			switch (m) {
			case DEGREE: 
				return ds.getVertexScore(v);
			case BETWEENNESS:
				return bc.getVertexScore(v);
			case CLOSENESS:
				return cc.getVertexScore(v);
			case EIGEN:
				return ec.getVertexScore(v);
			default:
				return 0.0;
			}
		}
}
