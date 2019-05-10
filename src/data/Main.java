package data;

import java.io.IOException;
import java.util.Set;

import calculations.Corelation;
import calculations.Decomposition;
import calculations.Giant;
import calculations.MetricData;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class Main {
	public static void main(String[] args) {
		try {
			//loading of data
			long start=System.currentTimeMillis();
			System.out.println("Start.");
			System.out.println("Processing input data:");
			Graph g = new Graph();
			Giant<Integer, String> gigant=new Giant<Integer,String>(g.getGraph());
			Set<Integer> giga=gigant.giant();
			
			//filtering the giga component
			Graph graph2=Graph.filter(giga);
			MetricData<Integer, String> data=new MetricData<Integer, String>(graph2.getGraph());
			System.out.println("Giant component of original graph have "+graph2.getGraph().getVertexCount()+" nodes");
			System.out.println("Input data calculated.");
			System.out.println("-----------------------------------------------------------------------------");
			
			//Pearsons and Spearmans corelation
			System.out.println("First functionality:Spearmans and Pearsons corelation");
			Corelation<Integer, String> cor=new Corelation<>(data);
			cor.calculate(Metrics.EIGEN);
			System.out.println("Pearsons corelation for eigen is "+String.format("%1.3f",cor.getPearsons()));
			System.out.println("Spearmans corelation for eigen is "+String.format("%1.3f",cor.getSpearmans()));
			System.out.println("*******************************************************************************");
			cor.calculate(Metrics.BETWEENNESS);
			System.out.println("Pearsons corelation for betweenness is "+String.format("%1.3f",cor.getPearsons()));
			System.out.println("Spearmans corelation for betweenness is "+String.format("%1.3f",cor.getSpearmans()));
			System.out.println("*******************************************************************************");
			cor.calculate(Metrics.CLOSENESS);
			System.out.println("Pearsons corelation for cleseness is "+String.format("%1.3f",cor.getPearsons()));
			System.out.println("Spearmans corelation for closeness is "+String.format("%1.3f",cor.getSpearmans()));
			System.out.println("*******************************************************************************");
			cor.calculate(Metrics.DEGREE);
			System.out.println("Pearsons corelation for degree is "+String.format("%1.3f",cor.getPearsons()));
			System.out.println("Spearmans corelation for degree is "+String.format("%1.3f",cor.getSpearmans()));
			System.out.println("*******************************************************************************");
			System.gc();
			System.out.println("-----------------------------------------------------------------------------");
			
			//decomposition of giga component
			System.out.println("Second functionality: decomposition");
			Decomposition<Integer,String>decomposition=new Decomposition<>(data);	
			System.out.println("-----------------------------------------------------------------------------");
			//10% by betweenness
			System.out.println("Third functionality: whithout 10% of most in-betweenn links.");
			System.out.println();
			UndirectedSparseGraph<Integer, String> tenp=decomposition.collect();
			System.out.println("The most central 10% of links has been found.");
			Graph g3=new Graph();
			g3.setGraph(tenp);
			Giant<Integer, String> tenpData=new Giant<Integer, String>(g3.getGraph());
			System.out.println("Number of components in new graph is "+tenpData.numberOfComponents());
			System.out.println("Number of one-node components in that subgraph is "+tenpData.numberOfOEComponents()+"(isolated)");
			int[] stats=tenpData.gigaStats();
			System.out.println("In gigant component of that subgraph are "+stats[0]+" nodes and "+stats[1]+" links.");
			System.out.println("-----------------------------------------------------------------------------");
			System.out.println("End of program.");
			System.out.println("Time of working=" + (System.currentTimeMillis()-start)/60000.00+"  minutes.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
