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
//			Corelation<Integer, String> cor=new Corelation(g2.getGraph(), Metrics.DEGREE);
//			System.out.println("Pearsons corelation for degree is "+String.format("%1.3f",cor.getPearsons()));
//			System.out.println("Spearmans corelation for degree is "+String.format("%1.3f",cor.getSpearmans()));
//		
			//decomposition of giga component
			Decomposition<Integer,String>decomposition=new Decomposition(g2.getGraph());
			
			//koristi klase dec i giga(unapredi giga) iz dec vrati skup cvorova
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
