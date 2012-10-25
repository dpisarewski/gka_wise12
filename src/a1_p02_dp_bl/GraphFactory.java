package a1_p02_dp_bl;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

public class GraphFactory {

	public static Graph<Object, GraphEdge> createGraph(boolean directed, boolean weighted, boolean labeled) {
		Graph<Object, GraphEdge> g = null;
		if (!directed && !weighted) {
			g = new SimpleGraph<Object, GraphEdge>(GraphEdge.class);
		} else if (!directed && weighted) {
			g = new SimpleWeightedGraph<Object, GraphEdge>(GraphEdge.class);
		} else if (directed && !weighted) {
			g = new DefaultDirectedGraph<Object, GraphEdge>(GraphEdge.class);
		} else if (directed && weighted) {
			g = new DefaultDirectedWeightedGraph<Object, GraphEdge>(GraphEdge.class);
		}
		return g;
	}
}
