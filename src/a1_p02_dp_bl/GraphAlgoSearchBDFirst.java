package a1_p02_dp_bl;

import java.util.*;

import a2_p02_dp_bl.GraphAlgoState;
import a2_p02_dp_bl.GraphAlgoStateDefault;

public class GraphAlgoSearchBDFirst<V, E> {	
	// Die verwendeten properties
	public final static String closedPropertyName = "closed";
	public final static String openPropertyName = "open";
	public final static String pathPropertyName = "path";
	
	// naming for the different phases of the algorithm
	public final static String initPhaseName = "Initialisierung";
	public final static String searchPhaseName = "Suche";
	public final static String pathPhaseName = "Pfaderstellung";
	public final static String endhPhaseName = "Ende";

	public final static String newCurrentVertexStepName = "Neuer aktueller Knoten";
	public final static String updateNeighboursStepName = "Nachbar upgedated";
	public final static String backtrackStepName = "Rekursion zur�ckgehen";
	public final static String addPathVertexStepName = "Neuen Knoten zum Pfad hinzugef�gt";
	
	
	GraphAlgoState<V, E> m_algoState = null;

	public GraphAlgoSearchBDFirst(GraphCommon<V,E> graph) {
		m_algoState = new GraphAlgoStateDefault(graph);
		m_algoState.createVertexPropertyType(closedPropertyName, GraphAlgoState.propertyTypeInteger, 0, Integer.MAX_VALUE);
		m_algoState.createVertexPropertyType(openPropertyName, GraphAlgoState.propertyTypeBoolean, 0, 1);
		m_algoState.createVertexPropertyType(pathPropertyName,GraphAlgoState.propertyTypeBoolean, 0, 1);
	}
	
	
	public GraphAlgoSearchBDFirst(GraphAlgoState<V, E> algoProperties) {
		m_algoState = algoProperties;
		m_algoState.createVertexPropertyType(closedPropertyName, GraphAlgoState.propertyTypeInteger, 0, Integer.MAX_VALUE);
		m_algoState.createVertexPropertyType(openPropertyName, GraphAlgoState.propertyTypeBoolean, 0, 1);
		m_algoState.createVertexPropertyType(pathPropertyName,GraphAlgoState.propertyTypeBoolean, 0, 1);
	}

	
	public int getAccessCounter() {
		return m_algoState.getAccessCounter();
	}

	public V getCurrentVertex() {
		return m_algoState.getCurrentVertex();
	}
	
	public void clearResults() {
		m_algoState.resetValues();
		//m_anzahlZugriffe = 0;
	}
	
	public GraphDefaultPath<V, E> depthFirst(V startVertex, V targetVertex) {
		m_algoState.resetValues();
		m_algoState.notifyOfNextPhase(initPhaseName);
		m_algoState.setVertexAsCurrent(startVertex);
		m_algoState.notifyOfNextPhase(searchPhaseName);
		GraphDefaultPath<V, E> path = depthFirstInternal(startVertex, targetVertex, 0);
		m_algoState.notifyOfNextPhase(endhPhaseName);
		return path;
	}

	
	protected GraphDefaultPath<V, E> depthFirstInternal(V startVertex, V targetVertex, int cost) {
		m_algoState.setVertexAsCurrent(startVertex);
		m_algoState.notifyOfNextStep(newCurrentVertexStepName);
		//Den Anfang in die Closed Liste einf�gen
		m_algoState.setVertexProperty(startVertex, closedPropertyName, cost);
		//Beenden, wenn Anfang das Ziel ist
		if (startVertex.equals(targetVertex)) {
			return (cost == 0) ? createPath(startVertex, targetVertex) : null;
		}
		//Nachbarknoten ermitteln
    	List<V> neighbours = getNeighbours(startVertex);
    	
    	//Iteration �ber Nachbarknoten
        for (V n : neighbours){
			Integer val = m_algoState.getVertexProperty(n, closedPropertyName);
        	//Wenn aktueller Knoten nicht in der Closed Liste, oder die kleiner als die bisherige ist
            if (val == null || val > (cost+1)) {
				//m_algoState.notifyOfNextStep(updateNeighboursStepName);
            	//Rufe die Suche rekursiv auf, bis der Zielknoten gefunden ist
            	depthFirstInternal(n, targetVertex, cost + 1);
        		m_algoState.setVertexAsCurrent(startVertex);
        		m_algoState.notifyOfNextStep(backtrackStepName);
            }
        }
        //Gebe einen leeren Pfad zur�ck, falls nichts gefunden
        return (cost == 0) ? createPath(startVertex, targetVertex) : null;
	}

	
	
	public GraphDefaultPath<V, E> breadthFirst(V startVertex, V targetVertex) {
		List<V> neighbours = new ArrayList<V>();
		
		m_algoState.resetValues();
		m_algoState.notifyOfNextPhase(initPhaseName);
		m_algoState.setVertexAsCurrent(startVertex);
		
		//Den Anfang in die Open Liste einf�gen
		m_algoState.setVertexProperty(startVertex, openPropertyName, true);
		// startwert 0
		m_algoState.setVertexProperty(startVertex, closedPropertyName, 0);
		m_algoState.addToAccessCounter(1);
		m_algoState.notifyOfNextPhase(searchPhaseName);

		
		//Iteration �ber Open Liste
		while(true) {
			//Nehme den N�chsten Knoten aus der Open Liste
			V v = m_algoState.getSomeVertexWithProperty(openPropertyName);
			m_algoState.setVertexAsCurrent(v);

			m_algoState.notifyOfNextStep(newCurrentVertexStepName);
			if (v==null) {
				// Open Liste ist leer, verlasse Schleife
				break;
			}
			// Knoten aus der Open Liste l�schen
			m_algoState.setVertexProperty(v, openPropertyName, null);

			//Beende, wenn aktueller Knoten der ZielKnoten ist
			if (v.equals(targetVertex)) {
				break;
			}
			
			// Wert des Knoten (Entfernung) aus der Closed List lesen
			Integer curVal = m_algoState.getVertexProperty(v, closedPropertyName);
			
			//Nachbarknoten ermitteln
			neighbours = getNeighbours(v);
			//Nachbarknoten in die Closed Liste einf�gen
			for(V n : neighbours) {
				Integer nval = m_algoState.getVertexProperty(n, closedPropertyName);
				if(nval == null) {
					// Knoten ist noch nicht in der Closed List eingetragen
					m_algoState.setVertexProperty(n, openPropertyName, true);
					m_algoState.setVertexProperty(n, closedPropertyName, new Integer(curVal + 1));
					m_algoState.notifyOfNextStep(updateNeighboursStepName);
				}
			}
		}
		//Erstelle einen Pfad
		GraphDefaultPath<V, E> path = createPath(startVertex, targetVertex);
		m_algoState.notifyOfNextPhase(endhPhaseName);
		return path;

	}
	
	

	
	protected GraphDefaultPath<V,E> createPath(V start, V vertex){
		m_algoState.notifyOfNextPhase(pathPhaseName);
		GraphDefaultPath<V, E> path = new GraphDefaultPath<V, E>(m_algoState.getGraph());
		//Anfangen mit dem Zielknoten
		V v = vertex;
		//Vorg�ngerknoten ermitteln
		List<V> predecessors = new ArrayList<V>();
		V previousVertex = vertex;
		//Solange es Knoten in der Closed Liste gibt
		while(v != null) {
			Integer val = m_algoState.getVertexProperty(v, closedPropertyName);
			if (val==null || val <= 0) {
				// Schleife abbrechen wenn nicht in open list, oder value <=0
				break;
			}
			//F�ge den aktuellen Knoten in den Pfad ein
			path.addVertex(v);
			if (!previousVertex.equals(v)) {
				E pathEdge = m_algoState.getGraph().getEdge(v, previousVertex);
				m_algoState.markEdgeAsChanged(pathEdge);
			}
			m_algoState.setVertexProperty(v, pathPropertyName, true);
			m_algoState.notifyOfNextStep(addPathVertexStepName);			
			//Vorg�ngerknoten ermitteln
			predecessors = filterByValue(v, val - 1);
			//Beenden, falls keine Vorg�ngerknoten
			if(predecessors == null)
				break;
			previousVertex = v;
			v = predecessors.get(0);	
		}
		//Den Anfangsknoten in den Pfad einf�gen
		path.addVertex(start);
		m_algoState.setVertexProperty(start, pathPropertyName, true);
		E pathEdge = m_algoState.getGraph().getEdge(start, previousVertex);
		m_algoState.markEdgeAsChanged(pathEdge);
		//Pfad umkehren
		return path.reverse();
	}
	
	//Filtert Closed Liste nach einem Wert
	protected List<V> filterByValue(V vertex, int filterValue){
		List<V> filteredVertices = new ArrayList<V>();
		// alle vorg�nger finden
		Set<V> predec = new HashSet<V>(m_algoState.getGraph().getPredecessors(vertex));
		// iteriere �ber alle vertices in der closed list
		for(V v : m_algoState.getVerticesWithProperty(closedPropertyName)) {
			m_algoState.addToAccessCounter(1);
			Integer val = m_algoState.getVertexProperty(v, closedPropertyName);
			if (val != null && val.intValue() == filterValue && predec.contains(v)) {
				// ein vorg�nger mit passenden wert wurde gefunden 
				filteredVertices.add(v);
			}
		}
		return filteredVertices;
	}
	
	//Ermittelt Nachbarknoten
	protected List<V> getNeighbours(V vertex) {
		List<V> neighbours = m_algoState.getGraph().getSuccessors(vertex);
		m_algoState.addToAccessCounter(neighbours.size());
		neighbours = new ArrayList<V>(new HashSet<V>(neighbours));
		//TODO Sortiere nach dem Gewicht der Kanten
		//Collections.sort(neighbours);
		return neighbours;
	}
	
	
}
