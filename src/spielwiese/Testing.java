package spielwiese;

import java.io.*;
import java.text.ParseException;

import a01_p02_dp_bl.*;

import org.jgrapht.graph.*;

public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GraphVertex<String> v1 = new GraphVertex<String>("v1");
		GraphVertex<String> v2 = new GraphVertex<String>("v1");
		
		if (v1.equals(v2)) {
			System.out.println("equal");
		} else {
			System.out.println("not equal");			
		}
		
		if (v1.hashCode() == v2.hashCode()) {
			System.out.println("hash equal");
		} else {
			System.out.println("hash not equal");			
		}
		
		FileReader fr;
		FileWriter fw;
		try {
	        GraphDirected gd = new GraphDirected(DefaultWeightedEdge.class);
			// read graph
			fr = new FileReader("graph1.gka");
			gd.deserialize(fr);
			fr.close();
			
	        System.out.println(gd.toString());

			fw = new FileWriter("graph1out.gka");
			gd.serialize(fw);
			fw.close();
	        
	        
	        GraphUndirected gu = new GraphUndirected(DefaultWeightedEdge.class);
			// read graph
			fr = new FileReader("graph2.gka");
			gu.deserialize(fr);
			fr.close();
			
	        // note undirected edges are printed as: {<v1>,<v2>}
	        System.out.println(gu.toString());
	
			fw = new FileWriter("graph2out.gka");
			gu.serialize(fw);
			fw.close();

	        
		}
		catch(IOException ex) {
			System.out.println(ex);			
		}
		catch(ParseException px) {
			System.out.println(px);
		}
	}

}


/*
GraphVertex<String> v1 = new GraphVertex<String>("v1");
GraphVertex<String> v2 = new GraphVertex<String>("v2");
GraphVertex<String> v3 = new GraphVertex<String>("v3");
GraphVertex<String> v4 = new GraphVertex<String>("v4");

// add the vertices
g.addVertex(v1);
g.addVertex(v2);
g.addVertex(v3);
g.addVertex(v4);

// add edges to create a circuit
g.addEdge(v1, v2);
g.addEdge(v2, v3);
g.addEdge(v3, v4);
g.addEdge(v4, v1);

// note undirected edges are printed as: {<v1>,<v2>}
System.out.println(g.toString());
*/
