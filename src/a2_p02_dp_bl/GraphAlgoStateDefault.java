package a2_p02_dp_bl;

import java.util.*;

import javax.swing.event.EventListenerList;

import a1_p02_dp_bl.GraphCommon;

public class GraphAlgoStateDefault<V, E> implements GraphAlgoState<V, E> {

	// accessible - properties per e/v (all props for this e/v)
	// accessible - properties per name (all e/vs for this property)
	int m_nextElementIndex = 0;
	int m_elementsCapacity = 128;
	
	GraphCommon<V,E> m_graph;
	int m_graphAccessCounter = 0;
	V m_currentVertex = null;
	
	Map<V, Integer> m_vertexToIndex = new HashMap<V, Integer>();
	ArrayList<V> m_indexToVertex = new ArrayList<V>(m_elementsCapacity);
	
	Map<E, Integer> m_edgeToIndex = new HashMap<E, Integer>();
	ArrayList<E> m_indexToEdge = new ArrayList<E>(m_elementsCapacity);
	
	Map<String, ArrayList<Object>> m_propertyToElement = new HashMap<String, ArrayList<Object>>();

	Map<String, String> m_propertyTypeName = new HashMap<String,String>();
	Map<String, Integer> m_propertyMinimum = new HashMap<String,Integer>();
	Map<String, Integer> m_propertyMaximum = new HashMap<String,Integer>();
	
	EventListenerList m_listeners = new EventListenerList(); 	
	String m_currentPhase = null;

	
	public GraphAlgoStateDefault(GraphCommon<V,E> graph) {
		m_graph = graph;
		resetValues();
	}
	
	protected int addGraphElement() {
		// add a new Element (Vertex or Edge)
		int index = m_nextElementIndex;
		if (m_nextElementIndex >= m_elementsCapacity) {
			// reserve more capacity
			m_elementsCapacity += 32;
			// TODO: extend(resize) all ArrayLists in m_propertyToElement, m_indexToEdge, m_indexToVertex
		}
		m_nextElementIndex++;
		return index;
	}
	
	@Override
	public void addToAccessCounter(int numAccesses) {
		m_graphAccessCounter += numAccesses;
	}
	@Override
	public int getAccessCounter() {
		return m_graphAccessCounter;
	}

	@Override
	public void createVertexPropertyType(String propName, String typename, int minVal, int maxVal) {
		ArrayList<Object> propertyValues = new ArrayList<Object>(m_elementsCapacity);
		for(int i=0; i<m_elementsCapacity; i++) {
			propertyValues.add(null);
		}
		m_propertyToElement.put(propName, propertyValues);
		m_propertyTypeName.put(propName,typename);
		m_propertyMinimum.put(propName, minVal);
		m_propertyMinimum.put(propName, maxVal);
	}

	@Override
	public void createEdgePropertyType(String propName, String typename, int minVal, int maxVal) {
		ArrayList<Object> propertyValues = new ArrayList<Object>(m_elementsCapacity);
		for(int i=0; i<m_elementsCapacity; i++) {
			propertyValues.add(null);
		}
		m_propertyToElement.put(propName, propertyValues);
		m_propertyTypeName.put(propName,typename);
		m_propertyMinimum.put(propName, minVal);
		m_propertyMinimum.put(propName, maxVal);
		
	}

	@Override
	public <P> void setVertexProperty(V vertex, String propName, P value) {
		// get the ArrayList for this property
		ArrayList<Object> propArray = m_propertyToElement.get(propName);
		// get the element index for this vertex (assign one if still unassigned)
		Integer index = m_vertexToIndex.get(vertex);
		if (index == null) {
			index = addGraphElement();
			m_vertexToIndex.put(vertex, index);
			m_indexToVertex.set(index, vertex);
		}
		propArray.set(index, value);
	    for ( GraphAlgoStepListener<V,E> l : m_listeners.getListeners( GraphAlgoStepListener.class ) ) {
	        l.vertexChanged(vertex);
	    }
	}

	@Override
	public <P >void setEdgeProperty(E edge, String propName, P value) {
		// get the ArrayList for this property
		ArrayList<Object> propArray = m_propertyToElement.get(propName);
		// get the element index for this edge (assign one if still unassigned)
		Integer index = m_edgeToIndex.get(edge);
		if (index == null) {
			index = addGraphElement();
			m_edgeToIndex.put(edge, index);
			m_indexToEdge.set(index, edge);
		}
		propArray.set(index, value);
	    for ( GraphAlgoStepListener<V,E> l : m_listeners.getListeners( GraphAlgoStepListener.class ) ) {
	        l.edgeChanged(edge);
	    }
	}

	@Override
	public void removeVertexProperty(V vertex, String propName) {
		// get the ArrayList for this property
		ArrayList<Object> propArray = m_propertyToElement.get(propName);
		// get the element index for this vertex (assign one if still unassigned)
		Integer index = m_vertexToIndex.get(vertex);
		if (index != null) {
			// vertex is assigned, nullify it
			propArray.set(index, null);
			m_indexToVertex.set(index, null);
		    for ( GraphAlgoStepListener<V,E> l : m_listeners.getListeners( GraphAlgoStepListener.class ) ) {
		        l.vertexChanged(vertex);
		    }
		}
	}

	@Override
	public void removeEdgeProperty(E edge, String propName) {
		// get the ArrayList for this property
		ArrayList<Object> propArray = m_propertyToElement.get(propName);
		// get the element index for this vertex (assign one if still unassigned)
		Integer index = m_edgeToIndex.get(edge);
		if (index != null) {
			// vertex is assigned, nullify it
			propArray.set(index, null);
			m_indexToEdge.set(index, null);
		    for ( GraphAlgoStepListener<V,E> l : m_listeners.getListeners( GraphAlgoStepListener.class ) ) {
		        l.edgeChanged(edge);
		    }
		}
	}

	@Override
	public <P> P getVertexProperty(V vertex, String propName) {
		// get the ArrayList for this property
		ArrayList<Object> propArray = m_propertyToElement.get(propName);
		// get the element index for this vertex (assign one if still unassigned)
		Integer index = m_vertexToIndex.get(vertex);
		if (index == null) {
			return null;
		}
		// vertex is assigned, return it's value
		@SuppressWarnings("unchecked")
		P p = (P)propArray.get(index);
		return p;
	}

	@Override
	public <P> P getEdgeProperty(E edge, String propName) {
		// get the ArrayList for this property
		ArrayList<Object> propArray = m_propertyToElement.get(propName);
		// get the element index for this edge (assign one if still unassigned)
		Integer index = m_edgeToIndex.get(edge);
		if (index == null) {
			return null;
		}
		// edge is assigned, return it's value
		@SuppressWarnings("unchecked")
		P p = (P)propArray.get(index);
		return p;
	}

	@Override
	public V getSomeVertexWithProperty(String propName) {
		ArrayList<Object> propertyObjects = m_propertyToElement.get(propName);
		for(int i=0; i < propertyObjects.size(); i++) {
			if (propertyObjects.get(i) != null) {
				V v = m_indexToVertex.get(i);
				if (v != null) {
					return v;
				}
			}
		}		
		return null;
	}
	
	// returns a list of all vertices that have the given property attached to it
	@Override
	public ArrayList<V> getVerticesWithProperty(String propName) {
		ArrayList<V> resultVertices = new ArrayList<V>();
		ArrayList<Object> propertyObjects = m_propertyToElement.get(propName);
		for(int i=0; i < propertyObjects.size(); i++) {
			if (propertyObjects.get(i) != null) {
				V v = m_indexToVertex.get(i);
				if (v != null) {
					resultVertices.add(v);
				}
			}
		}		
		return resultVertices;
	}



	@Override
	public E getSomeEdgeWithProperty(String propName) {
		ArrayList<Object> propertyObjects = m_propertyToElement.get(propName);
		for(int i=0; i < propertyObjects.size(); i++) {
			if (propertyObjects.get(i) != null) {
				E e = m_indexToEdge.get(i);
				if (e != null) {
					return e;
				}
			}
		}		
		return null;
	}
	
	
	@Override
	public ArrayList<E> getEdgesWithProperty(String propName) {
		ArrayList<E> resultEdges = new ArrayList<E>();
		ArrayList<Object> propertyObjects = m_propertyToElement.get(propName);
		for(int i=0; i < propertyObjects.size(); i++) {
			if (propertyObjects.get(i) != null) {
				E e = m_indexToEdge.get(i);
				if (e != null) {
					resultEdges.add(e);
				}
			}
		}		
		return resultEdges;
	}

	@Override
	public void markVertexAsChanged(V vertex) {
	    for ( GraphAlgoStepListener<V,E> l : m_listeners.getListeners( GraphAlgoStepListener.class ) ) {
	        l.vertexChanged(vertex);
	    }
	}

	@Override
	public void markEdgeAsChanged(E edge) {
	    for ( GraphAlgoStepListener<V,E> l : m_listeners.getListeners( GraphAlgoStepListener.class ) ) {
	        l.edgeChanged(edge);
	    }
	}	
	
	@Override
	public void setVertexAsCurrent(V vertex) {
		if (m_currentVertex != null) {
			markVertexAsChanged(m_currentVertex);
		}
		m_currentVertex = vertex;
		if (m_currentVertex != null) {
			markVertexAsChanged(m_currentVertex);
		}
	}
	
	@Override
	public V getCurrentVertex() {
		return m_currentVertex;
	}
	
	

	public GraphCommon<V, E> getGraph() {
		return m_graph;
	}

	
	public void resetValues() {
		int i;
		m_graphAccessCounter = 0;
		m_nextElementIndex = 0;
		m_elementsCapacity = 128;
		m_vertexToIndex.clear();
		m_indexToVertex.clear();
		m_indexToEdge.clear();
		for(i=0; i<m_elementsCapacity; i++) {
			m_indexToVertex.add(null);
			m_indexToEdge.add(null);
		}
		for(String propertyName : m_propertyToElement.keySet()) {
			ArrayList<Object> propertyValues = m_propertyToElement.get(propertyName);
			propertyValues.clear();
			for(i=0; i<m_elementsCapacity; i++) {
				propertyValues.add(null);
			}			
		}
		
		// TODO:  clear below  also, or just remove the arraylists ?
		//m_propertyToElement.clear();
		//m_propertyTypeName.clear();
		//m_propertyMinimum.clear();
		//m_propertyMaximum.clear();		
	}

	@Override
	public void addGraphAlgoStateListener(GraphAlgoStepListener<V,E> listener ) { 
		m_listeners.add( GraphAlgoStepListener.class, listener ); 
	} 
 
	@Override
	public void removeGraphAlgoStateListener(GraphAlgoStepListener<V,E> listener ) { 
		m_listeners.remove( GraphAlgoStepListener.class, listener ); 
	}
	
	@Override
	public void registerPhase(String phaseName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void registerStep(String phaseName, String stepName) {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public boolean notifyOfNextPhase(String phaseName) {
		m_currentPhase = phaseName;
	    for ( GraphAlgoStepListener<V,E> l : m_listeners.getListeners( GraphAlgoStepListener.class ) ) {
	        l.algoSingleStep(phaseName, null);
	    }
	    return false;
	}

	@Override
	public boolean notifyOfNextStep(String stepName) {
		// TODO Auto-generated method stub
	    for ( GraphAlgoStepListener<V,E> l : m_listeners.getListeners( GraphAlgoStepListener.class ) ) {
	        l.algoSingleStep(m_currentPhase, stepName);
	    }
		return false;
	}





}
