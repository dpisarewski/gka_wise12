package a2_p02_dp_bl;

import java.util.ArrayList;

import a1_p02_dp_bl.GraphCommon;

public interface GraphAlgoState<V, E> {
	String propertyTypeBoolean  = "Boolean";
	String propertyTypeInteger  = "Integer";
	String propertyTypeGraphVertex  = "GraphVertex";
	
	public void createVertexPropertyType(String propName, String typename, int minVal, int maxVal);
	public void createEdgePropertyType(String propName, String typename, int minVal, int maxVal);
	
	public <P> void setVertexProperty(V vertex, String propName, P value);
	public <P> void setEdgeProperty(E edge, String propName, P value);
	
	public void removeVertexProperty(V vertex, String propName);
	public void removeEdgeProperty(E edge, String propName);

	public <P> P getVertexProperty(V vertex, String propName);
	public <P> P getEdgeProperty(E edge, String propName);

	public ArrayList<V> getVerticesWithProperty(String propName);
	public V getSomeVertexWithProperty(String propName);
	public ArrayList<E> getEdgesWithProperty(String propName);
	public E getSomeEdgeWithProperty(String propName);
	
	public void markVertexAsChanged(V vertex);
	public void markEdgeAsChanged(E edge);
	
	public void setVertexAsCurrent(V vertex);
	public V getCurrentVertex();	
	
	public GraphCommon<V, E> getGraph();
	
	public void resetValues();
	
	public void addToAccessCounter(int numAccesses);
	public int getAccessCounter();
	
	public void addGraphAlgoStateListener(GraphAlgoStepListener<V,E> listener);
	public void removeGraphAlgoStateListener(GraphAlgoStepListener<V,E> listener);
	
	public void registerPhase(String phaseName);
	public void registerStep(String phaseName, String stepName);
	
	public boolean notifyOfNextPhase(String phaseName);
	public boolean notifyOfNextStep(String stepName);
	
	// TODO: remove all properties for a vertex / edge ?
	
	// TODO: add paths for visualization (solutions?)
}

