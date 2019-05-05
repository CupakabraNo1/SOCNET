package zadatak2;

import java.io.IOException;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		try {
			//loading of data
			Graph g = new Graph();
			Gigant<Integer, String> gigant=new Gigant<Integer,String>(g.getGraph());
			Set<Integer> giga=gigant.gigant();
			
			//filtering the giga component
			Graph g2=g.filter(giga);
			
			//Pearsons and Spearmans corelation
//			System.out.println("Calculating...");
//			Corelation<Integer, String> cor=new Corelation(g2.getGraph());
//			cor.calculate(Metrics.EIGEN);
//			System.out.println("Pearsons corelation for eigen is "+String.format("%1.3f",cor.getPearsons()));
//			System.out.println("Spearmans corelation for eigen is "+String.format("%1.3f",cor.getSpearmans()));
//			System.out.println("*******************************************************************************");
//			cor.calculate(Metrics.BETWEENNESS);
//			System.out.println("Pearsons corelation for betweenness is "+String.format("%1.3f",cor.getPearsons()));
//			System.out.println("Spearmans corelation for betweenness is "+String.format("%1.3f",cor.getSpearmans()));
//			System.out.println("*******************************************************************************");
//			cor.calculate(Metrics.CLOSENESS);
//			System.out.println("Pearsons corelation for cleseness is "+String.format("%1.3f",cor.getPearsons()));
//			System.out.println("Spearmans corelation for closeness is "+String.format("%1.3f",cor.getSpearmans()));
//			System.out.println("*******************************************************************************");
//			cor.calculate(Metrics.DEGREE);
//			System.out.println("Pearsons corelation for degree is "+String.format("%1.3f",cor.getPearsons()));
//			System.out.println("Spearmans corelation for degree is "+String.format("%1.3f",cor.getSpearmans()));
//			System.out.println("*******************************************************************************");
			
			//decomposition of giga component
			Decomposition<Integer,String>decomposition=new Decomposition(g2.getGraph());	
			
			//10% by betweenness
			Set<Integer> tenp=decomposition.collect();
			System.out.println("The most central 10% of nodes has been found.");
			Graph g3=Graph.filter(tenp);
			Gigant<Integer, String> tenpData=new Gigant<Integer, String>(g3.getGraph());
			System.out.println("Number of components in graph representing 10% of most central nodes is "+tenpData.numberOfComponents());
			System.out.println("Number of one-node components in that subgraph is "+tenpData.numberOfOEComponents()+" ( "+tenpData.isolated()+" are isolated). ");
			int[] stats=tenpData.gigaStats();
			System.out.println("In gigant component of that subgraph are "+stats[0]+" nodes and "+stats[1]+" links.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
