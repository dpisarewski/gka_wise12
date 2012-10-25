package a01_p02_bl_dp;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Application {

	public Application() {
	}
	
	public static void main(String[] args){
		Application prog = new Application();
		GraphDirected 	gd = new GraphDirected(DefaultWeightedEdge.class);
		GraphUndirected gu = new GraphUndirected(DefaultWeightedEdge.class);
		gd.load("graph1.gka");
		gu.load("graph2.gka");
		GraphDefaultPath pathD = (GraphDefaultPath) gd.find(new GraphVertex("a"), new GraphVertex("i"), "BFS");
		System.out.println(pathD.toString());
		
		GraphDefaultPath pathU = (GraphDefaultPath) gu.find(new GraphVertex("a"), new GraphVertex("a"), "DFS");
		System.out.println(pathU.toString());
	}

}
