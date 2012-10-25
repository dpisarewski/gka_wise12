package a1_p02_dp_bl;

import org.apache.commons.lang3.*;
import org.jgrapht.*;

public class Demo {

	public static void main(String[] args) {
		String filename = "graph.gka";
		Demo prog = new Demo();
		Graph ug = GraphFactory.createGraph(false, false, false);
		prog.initializeGraph(ug);
		prog.saveGraph(ug, filename);
		ug = prog.loadGraph(filename);
		prog.drawGraph(ug);
		prog.serializeGraph(ug);
	}

	private String serializeGraph(Graph<Object, GraphEdge> g) {
		String result = "";
		if (!(g instanceof DirectedGraph) && !(g instanceof WeightedGraph)) {
			result = "#ungerichtet\n\r" + StringUtils.join(g.edgeSet(), ",");
		} else if (!(g instanceof DirectedGraph) && (g instanceof WeightedGraph)) {
			result = "#gerichtet\n\r" + "#gewichtet\n\r" + StringUtils.join(g.edgeSet(), ",");
		} else if ((g instanceof DirectedGraph) && !(g instanceof WeightedGraph)) {
			result = "#ungerichtet\n\r" + StringUtils.join(g.edgeSet(), ",");
		} else if ((g instanceof DirectedGraph) && (g instanceof WeightedGraph)) {
			result = "#gerichtet\n\r" + "#gewichtet\n\r" + StringUtils.join(g.edgeSet(), ",");
		}
		return result;
	}

	private void initializeGraph(Graph<Object, GraphEdge> ug) {
		String v1 = "Berlin";
		String v2 = "Bremen";
		String v3 = "Hamburg";
		ug.addVertex(v1);
		ug.addVertex(v2);
		ug.addVertex(v3);
		GraphEdge e1 = ug.addEdge(v1, v2);
		GraphEdge e2 = ug.addEdge(v2, v3);
		GraphEdge e3 = ug.addEdge(v3, v1);
		if (ug instanceof WeightedGraph) {
			((WeightedGraph) ug).setEdgeWeight(e1, 450.0);
			((WeightedGraph) ug).setEdgeWeight(e2, 123.0);
			((WeightedGraph) ug).setEdgeWeight(e3, 289.0);
		}
	}

	private void drawGraph(Graph ug) {
		
	}

	private Graph loadGraph(String filename) {
		return null;
	}

	private void saveGraph(Graph g, String filename) {

	}

}
