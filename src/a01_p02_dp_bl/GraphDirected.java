package a01_p02_dp_bl;

import java.io.*;
import java.text.ParseException;
import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

// svn+ssh://leppin_b@svn.informatik.haw-hamburg.de/srv/svn/p02_dp_bl/

//es muss ein Pseudograph als Basisklasse sein, weil sonst keine Schleifen erlaubt sind (graph1.gka enthält eine Schleife)
public class GraphDirected extends DirectedPseudograph<GraphVertex, DefaultWeightedEdge> implements GraphSerialization
{
	private static final long serialVersionUID = -2586742127931932763L;
	boolean m_hasWeights = false;
	boolean m_hasAttributes = false;

	int m_accessCounter = 0;

	public GraphDirected() {
		super(DefaultWeightedEdge.class);
	}

	public void serialize(Writer out) throws IOException {
		BufferedWriter writer = new BufferedWriter(out);
		java.util.Set<DefaultWeightedEdge> edges = edgeSet();

		writer.write("#gerichtet");
		writer.newLine();
		if (m_hasWeights && m_hasAttributes) {
			writer.write("#attributiert,gewichtet");
			writer.newLine();

		} else if (m_hasAttributes) {
			writer.write("#attributiert");
			writer.newLine();
		} else if (m_hasWeights) {
			writer.write("#gewichtet");
			writer.newLine();
		} else {

		}
		for ( DefaultWeightedEdge edge : edges) {

			writer.write(getEdgeSource(edge).getName());
			if (m_hasAttributes) {
				writer.write(",");
				writer.write(getEdgeSource(edge).getAttribute().toString());
			}

			writer.write(",");
			writer.write(getEdgeTarget(edge).getName());
			if (m_hasAttributes) {
				writer.write(",");
				writer.write(getEdgeTarget(edge).getAttribute().toString());
			}

			if (m_hasWeights) {
				writer.write(",");
				double weight = getEdgeWeight(edge);
				if ( ((double)((int)weight)) == weight) {
					// speichere integer werte
					writer.write( Integer.toString((int)weight) );
				} else {
					writer.write( Double.toString(weight) );
				}
			}
			writer.newLine();
		}
		writer.flush();
	}
	@SuppressWarnings("unchecked")
	public void deserialize(Reader in) throws IOException, ParseException {
		String line;
		boolean inHeader = true;
		boolean dirSpecified = false;
		LineNumberReader lineReader =  new LineNumberReader(in);
		lineReader.setLineNumber(0);

		// Graph erst leeren (Alle evtl. vorhandenen Edges und Vertices löschen )
		// in zwei schritten, da iterieren auf dem Set während des löschens Fehler verursacht
		DefaultWeightedEdge[] edges = edgeSet().toArray(new DefaultWeightedEdge[0]);
		removeAllEdges(edges);
		Object[] vertices = vertexSet().toArray();
		for(Object v : vertices) {
			removeVertex((GraphVertex<String>)v);
		}

		line = lineReader.readLine();
		while(line != null) {
			line = line.trim();
			// skip empty lines
			if (!line.isEmpty()) {
				if (line.startsWith("#")) {
					if (!inHeader) {
						throw new ParseException("Line #" + lineReader.getLineNumber() +": Encountered \"#\" outside graph header.", lineReader.getLineNumber());
					}
					line = line.substring(2);
					String[] headerIdentifier = line.split(",");
					for(String hi : headerIdentifier) {
						if (hi.equalsIgnoreCase("ungerichtet")) {
							throw new ParseException("Line #" + lineReader.getLineNumber() +": Identifier \"ungerichtet\" while reading directed graph.", lineReader.getLineNumber());
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
					// Das sollte eine Zeile mit Knoten + Edge informationen sein
					// damit sind wir auf jeden Fall nicht mehr im Header
					inHeader = false;
					String[] kantenParameter = line.split(",");

					GraphVertex<String> vsrc = null;
					GraphVertex<String> vdst = null;
					double weight = 0.0;
					if (!m_hasWeights && !m_hasAttributes) {
						// Wir erwarten genau 2 parameter
						if (kantenParameter.length != 2) {
							throw new ParseException("Line #" + lineReader.getLineNumber() +" : Invalid edge definition.", lineReader.getLineNumber());
						}
						vsrc = new GraphVertex<String>(kantenParameter[0].trim());
						vdst = new GraphVertex<String>(kantenParameter[1].trim());
					} else if (m_hasWeights && !m_hasAttributes) {
						// Gewichtet
						// Wir erwarten genau 3 parameter
						if (kantenParameter.length != 3) {
							throw new ParseException("Line #" + lineReader.getLineNumber() +" : Invalid edge definition.", lineReader.getLineNumber());
						}
						vsrc = new GraphVertex<String>(kantenParameter[0].trim());
						vdst = new GraphVertex<String>(kantenParameter[1].trim());
						weight =  Double.parseDouble(kantenParameter[2].trim());
					} else if (!m_hasWeights && m_hasAttributes) {
						// Attributiert
						// Wir erwarten genau 4 parameter
						if (kantenParameter.length != 4) {
							throw new ParseException("Line #" + lineReader.getLineNumber() +" : Invalid edge definition.", lineReader.getLineNumber());
						}
						vsrc = new GraphVertex<String>(kantenParameter[0].trim(), kantenParameter[1].trim());
						vdst = new GraphVertex<String>(kantenParameter[2].trim(), kantenParameter[3].trim());
					} else {
						// Attributiert und gewichtet
						// Wir erwarten genau 5 parameter
						if (kantenParameter.length != 5) {
							throw new ParseException("Line #" + lineReader.getLineNumber() +" : Invalid edge definition.", lineReader.getLineNumber());
						}
						vsrc = new GraphVertex<String>(kantenParameter[0].trim(), kantenParameter[1].trim());
						vdst = new GraphVertex<String>(kantenParameter[2].trim(), kantenParameter[3].trim());
						weight =  Double.parseDouble(kantenParameter[4].trim());
					}

					// add source vertex
					if (addVertex(vsrc)) {
						//System.out.println("Added new Vertex " + vsrc.toString());
					} else {
						//System.out.println("Existing Vertex " + vsrc.toString());
					}
					// add destination vertex
					if (addVertex(vdst)) {
						//System.out.println("Added new Vertex " + vdst.toString());
					} else {
						//System.out.println("Existing Vertex " + vdst.toString());
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

	public Graph load(String filename) {
		try {
			deserialize(new FileReader(filename));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return this;
	}

	public void clearAttributes(){
		for(GraphVertex v : vertexSet()){
			v.setAttribute(null);
		}
	}

	private List<GraphVertex> getSuccessors(GraphVertex vertex) {
		countAccess();
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
		closed.remove(start);
		closed.put(start, cost);
		//Beenden, wenn Anfang das Ziel ist
		if (start.equals(vertex)) return cost == 0 ? createPath(start, vertex, closed) : null;
		//Nachbarknoten ermitteln
    	List<GraphVertex> neighbours = new ArrayList<GraphVertex>();
    	neighbours = getNeighbours(start, closed);

    	//Iteration über Nachbarknoten
        for (GraphVertex v : neighbours){
        	//Wenn aktueller Knoten nicht in der Closed Liste
            if (closed.get(v) == null || closed.get(v) > cost){
            	//Rufe die Suche rekursiv auf, und wenn der Zielknoten gefunden ist
            	depthFirst(v, vertex, closed, cost + 1);
            }
        }
        //Gebe einen leeren Pfad zurück, falls nichts gefunden
        return cost == 0 ? createPath(start, vertex, closed) : null;
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
				if(closed.get(n) == null || closed.get(n) > closed.get(v) + 1) closed.put(n, closed.get(v) + 1);
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
			//Vorgangerknoten ermitteln
			predecessors = filterByValue(v, closed, closed.get(v) - 1);
			//Beenden, falls keine Vorgängerknoten
			if(predecessors == null) break;
			//TODO Sortiere nach dem Gewicht der Kanten
			//Collections.sort(predecessors);
			v = predecessors.get(0);
		}
		//Den Anfangsknoten in den Pfad einfügen
		path.addVertex(start);
		//Pfad umkehren
		return path.reverse();
	}
	
	//Filtert Closed Liste nach einem Wert
	protected List<GraphVertex> filterByValue(GraphVertex vertex, Map<GraphVertex, Integer> map, Integer value){
		if(value == null) return null;
		List<GraphVertex> filteredVertices = new ArrayList<GraphVertex>();
		for(GraphVertex v : map.keySet()){
			if(map.get(v).equals(value) && Graphs.predecessorListOf(this, vertex).contains(v)) filteredVertices.add(v);
		}
		return filteredVertices;
	}
	
	//Ermittelt Nachbarknoten
	protected List<GraphVertex> getNeighbours(GraphVertex vertex, Map<GraphVertex, Integer> closed){
		List<GraphVertex> neighbours = new ArrayList<GraphVertex>();
		neighbours = getSuccessors(vertex);
		neighbours = new ArrayList(new HashSet(neighbours));
		//TODO Sortiere nach dem Gewicht der Kanten
		//Collections.sort(neighbours);
		return neighbours;
	}
	
	protected void countAccess(){
		m_accessCounter++; 
	}
	
	public int getAccessCount(){
		return m_accessCounter;
	}
	
	public void clearAccessCount(){
		m_accessCounter = 0;
	}


}