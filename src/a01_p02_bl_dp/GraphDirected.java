package a01_p02_bl_dp;

import java.io.*;
import java.text.ParseException;
import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.AbstractGraphIterator;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

//es muss ein Pseudograph als Basisklasse sein, weil sonst keine Schleifen erlaubt sind (graph1.gka enthдlt eine Schleife)
public class GraphDirected extends
		DirectedPseudograph<GraphVertex, DefaultWeightedEdge> {
	private static final long serialVersionUID = -2586742127931932763L;
	boolean m_hasWeights = false;
	boolean m_hasAttributes = false;

	public GraphDirected(Class<? extends DefaultWeightedEdge> edgeClass) {
		super(edgeClass);
		// TODO Auto-generated constructor stub
	}

	public void deserialize(Reader in) throws IOException, ParseException {
		String line;
		boolean inHeader = true;
		boolean dirSpecified = false;
		LineNumberReader lineReader = new LineNumberReader(in);
		lineReader.setLineNumber(1);

		// Graph erst leeren (Alle evtl. vorhandenen Edges und Vertices lцschen
		// )
		removeAllEdges(edgeSet());
		removeAllVertices(vertexSet());

		line = lineReader.readLine();
		while (line != null) {
			line = line.trim();
			// skip empty lines
			if (!line.isEmpty()) {
				if (line.startsWith("#")) {
					if (!inHeader) {
						throw new ParseException("Line #"
								+ lineReader.getLineNumber()
								+ ": Encountered \"#\" outside graph header.",
								lineReader.getLineNumber());
					}
					line = line.substring(1);
					String[] headerIdentifier = line.split(",");
					for (String hi : headerIdentifier) {
						if (hi.equalsIgnoreCase("ungerichtet")) {
							throw new ParseException(
									"Line #"
											+ lineReader.getLineNumber()
											+ ": Identifier \"ungerichtet\" while reading directed graph.",
									lineReader.getLineNumber());
						} else if (hi.equalsIgnoreCase("gerichtet")) {
							dirSpecified = true;
							// TODO: test later on
						} else if (hi.equalsIgnoreCase("gewichtet")) {
							m_hasWeights = true;
						} else if (hi.equalsIgnoreCase("attributiert]")) {
							m_hasAttributes = true;
						}
					}
				} else {
					// Das sollte eine Zeile mit Knoten + Edge informationen
					// sein
					// damit sind wir auf jeden Fall nicht mehr im Header
					inHeader = false;
					String[] kantenParameter = line.split(",");

					GraphVertex vsrc = null;
					GraphVertex vdst = null;
					double weight = 0.0;
					if (!m_hasWeights && !m_hasAttributes) {
						// Wir erwarten genau 2 parameter
						if (kantenParameter.length != 2) {
							throw new ParseException("Line #"
									+ lineReader.getLineNumber()
									+ " : Invalid edge definition.",
									lineReader.getLineNumber());
						}
						vsrc = new GraphVertex(kantenParameter[0].trim());
						vdst = new GraphVertex(kantenParameter[1].trim());
					} else if (m_hasWeights && !m_hasAttributes) {
						// Gewichtet
						// Wir erwarten genau 3 parameter
						if (kantenParameter.length != 3) {
							throw new ParseException("Line #"
									+ lineReader.getLineNumber()
									+ " : Invalid edge definition.",
									lineReader.getLineNumber());
						}
						vsrc = new GraphVertex(kantenParameter[0].trim());
						vdst = new GraphVertex(kantenParameter[1].trim());
						weight = Double.parseDouble(kantenParameter[2].trim());
					} else if (!m_hasWeights && m_hasAttributes) {
						// Attributiert
						// Wir erwarten genau 4 parameter
						if (kantenParameter.length != 4) {
							throw new ParseException("Line #" + lineReader.getLineNumber() + " : Invalid edge definition.",	lineReader.getLineNumber());
						}
						vsrc = new GraphVertex(kantenParameter[0].trim(), Integer.parseInt(kantenParameter[1].trim()));
						vdst = new GraphVertex(kantenParameter[2].trim(), Integer.parseInt(kantenParameter[3].trim()));
					} else {
						// Attributiert und gewichtet
						// Wir erwarten genau 5 parameter
						if (kantenParameter.length != 5) {
							throw new ParseException("Line #" + lineReader.getLineNumber() + " : Invalid edge definition.",	lineReader.getLineNumber());
						}
						vsrc = new GraphVertex(kantenParameter[0].trim(), Integer.parseInt(kantenParameter[1].trim()));
						vdst = new GraphVertex(kantenParameter[2].trim(), Integer.parseInt(kantenParameter[3].trim()));
						weight = Double.parseDouble(kantenParameter[4].trim());
					}

					// add source vertex
					if (addVertex(vsrc)) {
						// System.out.println("Added new Vertex " +
						// vsrc.toString());
					} else {
						// System.out.println("Existing Vertex " +
						// vsrc.toString());
					}
					// add destination vertex
					if (addVertex(vdst)) {
						// System.out.println("Added new Vertex " +
						// vdst.toString());
					} else {
						// System.out.println("Existing Vertex " +
						// vdst.toString());
					}
					DefaultWeightedEdge edge = addEdge(vsrc, vdst);
					if (m_hasWeights) {
						setEdgeWeight(edge, weight);
					}

				}
			}
			line = lineReader.readLine();
		}
	}

	public void load(String filename) {
		try {
			deserialize(new FileReader(filename));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void clearAttributes(){
		for(GraphVertex v : vertexSet()){
			v.setAttribute(null);
		}
	}
	
	private List<GraphVertex> getSuccessors(GraphVertex vertex) {
		return Graphs.successorListOf(this, vertex);
	}
	
	public GraphPath find(GraphVertex start, GraphVertex vertex, String alg) {
		if (alg.equals("BFS")) {
			// Wenn Breadth-First Suche
			return breadthFirst(start, vertex);
		} else if (alg.equals("DFS")) {
			// Wenn Depth-First Suche
			return depthFirst(start, vertex, new HashMap<GraphVertex, Integer>(), 0);
		}
		return null;
	}
	
	private GraphPath depthFirst(GraphVertex start, GraphVertex vertex, Map<GraphVertex, Integer> closed, int cost) {
		//Den Anfang in die Closed Liste einfügen
		closed.put(start, cost);
		//Beenden, wenn Anfang das Ziel ist
		if (start.equals(vertex)) return cost == 0 ? createPath(start, vertex, closed) : null;
		//Nachbarknoten ermitteln
    	List<GraphVertex> neighbours = new ArrayList<GraphVertex>();
    	neighbours = getNeighbours(start, closed);
    	
    	//Iteration über Nachbarknoten
        for (GraphVertex v : neighbours){
        	//Wenn aktueller Knoten nicht in der Closed Liste
            if (closed.get(v) == null){
            	//Rufe die Suche rekursiv auf, und wenn der Zielknoten gefunden ist
            	if(depthFirst(v, vertex, closed, cost + 1) == null){
            		//Beende falls nicht Anfang andernfalls erstelle einen Pfad
            		return cost == 0 ? createPath(start, vertex, closed) : null;
            	}
            }
        }
        //Gebe einen leeren Pfad zurück, falls nichts gefunden
        return new GraphDefaultPath<GraphVertex, DefaultWeightedEdge>(this);
	}

	protected GraphPath breadthFirst(GraphVertex start, GraphVertex vertex){
		List<GraphVertex> open = new ArrayList<GraphVertex>(); 
		Map<GraphVertex, Integer> closed = new HashMap<GraphVertex, Integer>();
		List<GraphVertex> neighbours = new ArrayList<GraphVertex>();
		//Den Anfang in die Open Liste einfügen
		open.add(start);
		//Den Anfang in die Closed Liste einfügen
		closed.put(start, 0);
		//Iteration über Open Liste
		while(!open.isEmpty()){
			//Nehme den Nächsten Knoten aus der Open Liste
			GraphVertex v = open.remove(0);
			//Beende, wenn aktueller Knoten der ZielKnoten ist
			if (v.equals(vertex)) break;
			//Nachbarknoten ermitteln
			neighbours = getNeighbours(v, closed);
			//Nachbarknoten in die Closed Liste einfügen
			for(GraphVertex n : neighbours){
				closed.put(n, closed.get(v) + 1);
			}
			open.addAll(neighbours);
		}
		//Erstelle einen Pfad
		return createPath(start, vertex, closed);
	}
	
	protected GraphPath createPath(GraphVertex start, GraphVertex vertex, Map<GraphVertex, Integer> closed){
		GraphDefaultPath<GraphVertex, DefaultWeightedEdge> path = new GraphDefaultPath<GraphVertex, DefaultWeightedEdge>(this);
		//Anfangen mit dem Zielknoten
		GraphVertex v = vertex;
		//Vorgängerknoten ermitteln
		List<GraphVertex> predecessors = new ArrayList<GraphVertex>();
		//Solange es Knoten in der Closed Liste gibt
		while(v != null && closed.get(v) > 0){
			//Füge den aktuellen Knoten in den Pfad ein
			path.addVertex(v);
			//Vorgängerknoten ermitteln
			predecessors = filterByValue(closed, closed.get(v) - 1);
			//Beenden, falls keine Vorgängerknoten
			if(predecessors == null) break;
			//TODO Sortiere nach dem Gewicht der Kanten 
			Collections.sort(predecessors);
			v = predecessors.get(0);
		}
		//Den Anfangsknoten in den Pfad einfügen
		path.addVertex(start);
		//Pfad umkehren
		return path.reverse();
	}
	
	//Filtert Closed Liste nach einem Wert
	protected List<GraphVertex> filterByValue(Map<GraphVertex, Integer> map, Integer value){
		if(value == null) return null;
		List<GraphVertex> filteredVertices = new ArrayList<GraphVertex>();
		for(GraphVertex v : map.keySet()){
			if(map.get(v).equals(value)) filteredVertices.add(v);
		}
		return filteredVertices;
	}
	
	//Ermittelt Nachbarknoten
	protected List<GraphVertex> getNeighbours(GraphVertex vertex, Map<GraphVertex, Integer> closed){
		List<GraphVertex> neighbours = new ArrayList<GraphVertex>();
		neighbours = getSuccessors(vertex);
		neighbours.removeAll(closed.keySet());
		neighbours = new ArrayList(new HashSet(neighbours));
		//TODO Sortiere nach dem Gewicht der Kanten
		Collections.sort(neighbours);
		return neighbours;
	}

}
