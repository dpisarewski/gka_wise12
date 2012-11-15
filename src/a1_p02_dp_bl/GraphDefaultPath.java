package a1_p02_dp_bl;

import java.util.*;
import org.jgrapht.*;

public class GraphDefaultPath<V, E> implements GraphPath<V, E> {

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
	
	public GraphDefaultPath(GraphDefaultPath<V,E> g){
		graph 				= g.graph;
		startVertex 		= (V) g.startVertex;
		endVertex 			= (V) g.endVertex;
		vertexList			= new LinkedList<V>(g.vertexList);
		edgeList			= new LinkedList<E>(g.edgeList);
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
	
	public List<V> getVertexList() {
		return vertexList;
	}

	public void setWeight(double w) {
		weight = w;
	}
	
	
	// implement GraphPath
	public double getWeight() {
		return weight;
	}
	
	public int length(){
		return vertexList.size() == 0 ? 0 : vertexList.size() - 1;
	}
	
	// aus http://stackoverflow.com/questions/187676/java-equivalents-of-c-sharp-string-format-and-string-join
	static String join(Collection<?> s, String delimiter) {
	     StringBuilder builder = new StringBuilder();
	     Iterator<?> iter = s.iterator();
	     while (iter.hasNext()) {
	         builder.append(iter.next());
	         if (!iter.hasNext()) {
	           break;                  
	         }
	         builder.append(delimiter);
	     }
	     return builder.toString();
	 }	
	
	// override Object
	public String toString() {
		return "Pfadlänge: " + length() + ". " + join(vertexList, " => ");
	}
	
	public void clear(){
		vertexList.clear();
		edgeList.clear();
	}
	
	public GraphDefaultPath<V,E> reverse(){
		GraphDefaultPath<V,E> newPath 	= new GraphDefaultPath<V,E>(this);
		Collections.reverse(newPath.edgeList);
		Collections.reverse(newPath.vertexList);
		newPath.startVertex = endVertex;
		newPath.endVertex = startVertex;
		return newPath;
	}

	// End GraphPathImpl.java

}
