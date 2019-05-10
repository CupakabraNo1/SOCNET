package calculations;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import data.Metrics;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class Corelation <V,E> {
	
	private UndirectedSparseGraph<V, E> graph;
	private MetricData<V, E> data;
	private double pearsons;
	private double spearmans;
	
	
	public double getPearsons() {
		return pearsons;
	}
	
	public double getSpearmans() {
		return spearmans;
	}
	
	public Corelation(MetricData<V, E> data) {
		this.data=data;
		this.graph=data.getGraph();
		
		
	}
	
	public void calculate(Metrics m) {
		long startTime=System.currentTimeMillis();
		corellations(m);
		long endTime = System.currentTimeMillis();
		System.out.println("Time of work="+ (endTime - startTime)/1000 + "seconds");

	}

	private void corellations(Metrics m) {
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
	
	private double metric(V v,Metrics m) {
			switch (m) {
			case DEGREE: 
				return data.getDs(v);
			case BETWEENNESS:
				return data.getBc(v);
			case CLOSENESS:
				return data.getCc(v);
			case EIGEN:
				return data.getEc(v);
			default:
				return 0.0;
			}
		}
}
