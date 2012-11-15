package a2_p02_dp_bl;

import a1_p02_dp_bl.GraphCommon;
import a1_p02_dp_bl.GraphDefaultPath;
import a1_p02_dp_bl.GraphVertex;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class GraphAlgoDijkstra {


	// closed list:
	public final static String closedPropertyName = "closed";
	public final static String openPropertyName = "open";
	public final static String pathPropertyName = "path";
	public final static String distancePropertyName = "g";
	public final static String predecessorPropertyName = "predecessor";

	// naming for the different phases of the A* algorithm
	public final static String initPhaseName = "Initialisierung";
	public final static String searchPhaseName = "Suche";
	public final static String pathPhaseName = "Pfaderstellung";
	public final static String endhPhaseName = "Ende";

	public final static String newCurrentVertexStepName = "Neuer aktueller Knoten";
	public final static String updateSuccessorStepName = "Neuer Nachfolger";
	public final static String addPathVertexStepName = "Neuen Knoten zum Pfad hinzugefügt";

	GraphCommon<GraphVertex, DefaultWeightedEdge> m_graph;
	GraphAlgoState<GraphVertex, DefaultWeightedEdge> m_algoState;
	// priority queue is used here for performance reasons, the property "openPropertyName" is a duplicate of this list, but without the ordering enforced by the priority queue class.
	PriorityQueue<OpenVertex> m_openList = new PriorityQueue<OpenVertex>(128);


	class OpenVertex implements Comparable<OpenVertex> {
		GraphVertex m_vertex;
		int m_fvalue;

		public OpenVertex(GraphVertex vertex, int fvalue) {
			m_vertex = vertex;
			m_fvalue = fvalue;
		}

		public GraphVertex getVertex() {
			return m_vertex;
		}

		public int getDistance() {
			return m_fvalue;
		}

		@Override
		public int compareTo(OpenVertex o) {
			return m_fvalue - o.m_fvalue;
		}

	}


	public GraphAlgoDijkstra(GraphCommon<GraphVertex, DefaultWeightedEdge> graph) {
		m_graph = graph;
		m_algoState = new GraphAlgoStateDefault<GraphVertex, DefaultWeightedEdge>(m_graph);
		createAlgoProperties();
	}

	public GraphAlgoDijkstra(GraphCommon<GraphVertex, DefaultWeightedEdge> graph, GraphAlgoState<GraphVertex, DefaultWeightedEdge> algoProperties) {
		m_graph = graph;
		m_algoState = algoProperties;
		createAlgoProperties();
	}
	
	protected void createAlgoProperties() {
		m_algoState.createVertexPropertyType(closedPropertyName,GraphAlgoState.propertyTypeBoolean, 0, 1);
		m_algoState.createVertexPropertyType(openPropertyName, GraphAlgoState.propertyTypeBoolean, 0, 1);
		m_algoState.createVertexPropertyType(pathPropertyName,GraphAlgoState.propertyTypeBoolean, 0, 1);
		m_algoState.createVertexPropertyType(distancePropertyName, GraphAlgoState.propertyTypeInteger, 0, Integer.MAX_VALUE);
		m_algoState.createVertexPropertyType(predecessorPropertyName, GraphAlgoState.propertyTypeGraphVertex, 0, 0);
	}

	public int getAccessCounter() {
		return m_algoState.getAccessCounter();
	}	
	
	public GraphVertex getCurrentVertex() {
		return m_algoState.getCurrentVertex();
	}

	public GraphDefaultPath<GraphVertex, DefaultWeightedEdge> search(GraphVertex start, GraphVertex target) {
		return dijkstra(start, target);
	}

	public GraphDefaultPath<GraphVertex, DefaultWeightedEdge> dijkstra(GraphVertex startVertex, GraphVertex destinationVertex) {
		// as the start and destination vertices do not necessarily contain the same attributes as the real vertex with the same name within the graph,
		// we look up the corresponding graph vertex and use it instead.
		int verticesFound = 0;
		m_algoState.setVertexAsCurrent(null);
		
		// Die start und endvertices besitzen i.d.R keine Attributwerte, deswegen die richtigen Vertices aus dem vertexSet heraussuchen
		for(GraphVertex v : m_graph.vertexSet()) {
			if (v.equals(startVertex)) {
				startVertex = v;
				verticesFound |= 1;
			}
			if (v.equals(destinationVertex)) {
				destinationVertex = v;
				verticesFound |= 2;
			}
		}
		
		
		// TODO: throw exception if vertices not inside graph
		// TODO: check if openlist has enough capacity

		// clear all saved properties
		m_openList.clear();
		m_algoState.resetValues();

		m_algoState.notifyOfNextPhase(initPhaseName);
		
		
		// add start vertex to open list
		m_openList.add(new OpenVertex(startVertex, 0));
		m_algoState.setVertexProperty(startVertex, openPropertyName, true);
		m_algoState.setVertexProperty(startVertex, distancePropertyName, 0);
		m_algoState.notifyOfNextPhase(searchPhaseName);

		if (startVertex == destinationVertex) {
			return null;
		}

		// continue while the open list still contains vertices to handle
		while(!m_openList.isEmpty()) {
			OpenVertex currentOVertex = m_openList.remove();

			m_algoState.setVertexAsCurrent(currentOVertex.getVertex());
			m_algoState.removeVertexProperty(m_algoState.getCurrentVertex(), openPropertyName);
			m_algoState.addToAccessCounter(1);
			m_algoState.notifyOfNextStep(newCurrentVertexStepName);
			
			if (m_algoState.getCurrentVertex().equals(destinationVertex)) {
                return createPath(startVertex);
            }
			// distance of shortest path from start to current vertex (g)
			int curDistance =  m_algoState.getVertexProperty(m_algoState.getCurrentVertex(), distancePropertyName);
			
			//m_algoProperties.notifyOfNextStep(checkVertexStepName);
            processSuccessors(curDistance);
        }
		m_algoState.notifyOfNextPhase(endhPhaseName);
		return null;
		
	}

    protected GraphDefaultPath<GraphVertex, DefaultWeightedEdge> createPath(GraphVertex startVertex) {
        // found the destination, exit and return path !
        m_algoState.notifyOfNextPhase(pathPhaseName);

        GraphVertex previousVertex = null;
        //int pathCost = 0;
        GraphDefaultPath<GraphVertex, DefaultWeightedEdge> path = new GraphDefaultPath<GraphVertex, DefaultWeightedEdge>(m_graph);
        // create optimal path by tracing backwards from destination to start using the "predecessor" property of vertices.
        int pathWeight = 0;
        do {
            m_algoState.setVertexProperty(m_algoState.getCurrentVertex(), pathPropertyName, true);
            m_algoState.addToAccessCounter(1);
            path.addVertex(m_algoState.getCurrentVertex());
            previousVertex = m_algoState.getVertexProperty(m_algoState.getCurrentVertex(), predecessorPropertyName);
            DefaultWeightedEdge pathEdge = m_graph.getEdge(previousVertex, m_algoState.getCurrentVertex());
            path.addEdge(pathEdge);
            pathWeight += m_graph.getEdgeWeight(pathEdge);
            //pathCost += m_graph.getEdgeWeight(pathEdge);
            m_algoState.notifyOfNextStep(addPathVertexStepName);
            m_algoState.markVertexAsChanged(m_algoState.getCurrentVertex());

            DefaultWeightedEdge edge = m_graph.getEdge(previousVertex, m_algoState.getCurrentVertex());
            m_algoState.markEdgeAsChanged(edge);

            m_algoState.setVertexAsCurrent(previousVertex);
        } while (m_algoState.getCurrentVertex() != startVertex);

        path.addVertex(m_algoState.getCurrentVertex());
        path.setWeight(pathWeight);
        path = path.reverse();
        m_algoState.setVertexProperty(m_algoState.getCurrentVertex(), pathPropertyName, true);
        m_algoState.setVertexAsCurrent(null);
        m_algoState.notifyOfNextPhase(endhPhaseName);
        return path;
    }

    protected void processSuccessors(int curDistance) {
        // get list of all possible successors
        List<GraphVertex> successors = m_graph.getSuccessors(m_algoState.getCurrentVertex());
        for(GraphVertex v : successors) {
            processSuccessor(curDistance, v);
        }
        m_algoState.setVertexProperty(m_algoState.getCurrentVertex(), closedPropertyName, true);
    }

    protected void processSuccessor(int curDistance, GraphVertex v) {
        m_algoState.addToAccessCounter(1);
        Boolean closedProperty =  m_algoState.getVertexProperty(v, closedPropertyName);
        // in closed list, continue with next successor
        if (closedProperty != null) return;

        // not in closed list
        // get edge connecting current vertex and successor
        DefaultWeightedEdge connectingEdge = m_graph.getEdge(m_algoState.getCurrentVertex(), v);
        int edgeDistance = (int)m_graph.getEdgeWeight(connectingEdge);

        // calc g for successor (distance from start vertex to successor vertex v using this path)
        int gDistance = curDistance + edgeDistance;

        // length of saved path
        Integer savedDistance =  m_algoState.getVertexProperty(v, distancePropertyName);
        if (savedDistance == null || gDistance < savedDistance) updateDistance(v, gDistance);
    }

    protected void updateDistance(GraphVertex v, int gDistance) {
        // no path length saved (and not in open list), add now and save this path as shortest
        // this path is shorter than the saved one
        // set this path as new shortest path from start vertex to successor vertex
        m_algoState.setVertexProperty(v, distancePropertyName, gDistance);

        // insert estimated total path leng	th into open list
        m_openList.add(new OpenVertex(v, gDistance));
        m_algoState.setVertexProperty(v, openPropertyName, true);

        // save predecessor !!
        m_algoState.setVertexProperty(v, predecessorPropertyName, m_algoState.getCurrentVertex());
        DefaultWeightedEdge edge = m_graph.getEdge(v, m_algoState.getCurrentVertex());
        m_algoState.markEdgeAsChanged(edge);

        //m_algoProperties.notifyOfNextStep(checkSuccessorStepName);
        m_algoState.notifyOfNextStep(updateSuccessorStepName);
    }


}
