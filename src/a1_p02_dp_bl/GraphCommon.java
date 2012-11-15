package a1_p02_dp_bl;

import java.util.List;

import org.jgrapht.Graph;

public interface GraphCommon<V, E> extends Graph<V,E> {
	public List<V> getSuccessors(V vertex);
	public List<V> getPredecessors(V vertex);

	public boolean isDirected();
	public boolean hasEdgeWeights();
	public boolean hasVertexAttributes();
	
	public void setEdgeWeight(E edge, double w);
	public double getEdgeWeight(E edge);
}
