package a01_p02_bl_dp;

import java.util.*;
import org.apache.commons.lang3.*;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultEdge;

public class GraphDefaultPath<V, E extends DefaultEdge> implements GraphPath<V, E> {

	// ~ Instance fields
	// --------------------------------------------------------

	private Graph<V, E> graph;

	private LinkedList<E> edgeList;
	
	private LinkedList<V> vertexList;

	private V startVertex;

	private V endVertex;

	private double weight;

	// ~ Constructors
	// -----------------------------------------------------------
	public GraphDefaultPath(Graph<V, E> graph) {
		this.graph = graph;
		this.startVertex = null;
		this.endVertex = null;
		this.edgeList = new LinkedList<E>();
		this.vertexList = new LinkedList<V>();
		this.weight = 0;
	}
	
	public GraphDefaultPath(GraphDefaultPath g){
		graph 				= g.graph;
		startVertex 		= (V) g.startVertex;
		endVertex 			= (V) g.endVertex;
		vertexList 			= (LinkedList) g.vertexList.clone();
		edgeList 			= (LinkedList) g.edgeList.clone();
		weight 				= g.weight;
	}

	public GraphDefaultPath(Graph<V, E> graph, V startVertex, V endVertex,
			LinkedList<E> edgeList, double weight) {
		this.graph = graph;
		this.startVertex = startVertex;
		this.endVertex = endVertex;
		this.edgeList = edgeList;
		this.vertexList = new LinkedList<V>();
		this.weight = weight;
	}

	// ~ Methods
	// ----------------------------------------------------------------
	
	public void addEdge(E edge){
		edgeList.add(edge);
	}
	
	public void addVertex(V vertex){
		if(vertexList.isEmpty()) startVertex = vertex;
		endVertex = vertex;
		vertexList.add(vertex);
	}

	// implement GraphPath
	public Graph<V, E> getGraph() {
		return graph;
	}

	// implement GraphPath
	public V getStartVertex() {
		return startVertex;
	}

	// implement GraphPath
	public V getEndVertex() {
		return endVertex;
	}

	// implement GraphPath
	public List<E> getEdgeList() {
		return edgeList;
	}

	// implement GraphPath
	public double getWeight() {
		return weight;
	}
	
	public int length(){
		return vertexList.size() == 0 ? 0 : vertexList.size() - 1;
	}

	// override Object
	public String toString() {
		return "Pfadlänge: " + length() + ". " + StringUtils.join(vertexList, " => ");
	}
	
	public void clear(){
		vertexList.clear();
		edgeList.clear();
	}
	
	public GraphDefaultPath reverse(){
		GraphDefaultPath newPath 	= new GraphDefaultPath(this);
		Collections.reverse(newPath.edgeList);
		Collections.reverse(newPath.vertexList);
		return newPath;
	}

	// End GraphPathImpl.java

}
