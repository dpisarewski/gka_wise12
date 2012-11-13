package a01_p02_dp_bl;

import java.io.*;

import java.text.ParseException;

import a01_p02_dp_bl.interfaces.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.*;

// es muss ein Pseudograph als Basisklasse sein, weil sonst keine Schleifen erlaubt sind (graph2.gka enthält eine Schleife)
public class GraphUndirected extends WeightedPseudograph<GraphVertex, DefaultWeightedEdge> implements GraphSerialization, CustomGraph
{
	private static final long serialVersionUID = -5976890015116381006L;
	boolean m_hasWeights = false;
	boolean m_hasAttributes = false;
	
	int m_accessCounter = 0;
	
	public GraphUndirected() {
		super(DefaultWeightedEdge.class);
	}

	@Override
	public void serialize(Writer out) throws IOException {
		BufferedWriter writer = new BufferedWriter(out);
		java.util.Set<DefaultWeightedEdge> edges = edgeSet();
		
		writer.write("#ungerichtet");
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
	@Override
	public void deserialize(Reader in) throws IOException, ParseException {
		String line;
		boolean inHeader = true;
		boolean dirSpecified = false;
		LineNumberReader lineReader =  new LineNumberReader(in);
		lineReader.setLineNumber(0);
		
		// Graph erst leeren (Alle evtl. vorhandenen Edges und Vertices läschen )		
		// in zwei schritten, da iterieren auf dem Set während des läschens Fehler verursacht
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
							dirSpecified = true;
							// TODO: test later on
						} else if (hi.equalsIgnoreCase("gerichtet")) {
							throw new ParseException("Line #" + lineReader.getLineNumber() +": Identifier \"gerichtet\" while reading undirected graph.", lineReader.getLineNumber());
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

	//Lädt einen gespeicherten Graph aus einer Datei
	public CustomGraph load(String filename){
		try {
			deserialize(new FileReader(filename));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return this;
	}

    public GraphPath find(GraphVertex start, GraphVertex vertex, String alg) {
        return GraphUtils.find(this, start, vertex, alg);
    }

	protected GraphPath createPath(GraphVertex start, GraphVertex vertex, Map<GraphVertex, Integer> closed){
		GraphDefaultPath<GraphVertex, DefaultWeightedEdge> path = new GraphDefaultPath<GraphVertex, DefaultWeightedEdge>(this);
		//Anfangen mit dem Zielknoten
		GraphVertex v = vertex;
		//Vorgängerknoten ermitteln
		List<GraphVertex> predecessors = new ArrayList<GraphVertex>();
		//Solange es Knoten in der Closed Liste gibt
		while(v != null && closed.get(v) > 0){
			//Fäge den aktuellen Knoten in den Pfad ein
			path.addVertex(v);
			//Vorgängerknoten ermitteln
			predecessors = filterByValue(v, closed, closed.get(v) - 1);
			//Beenden, falls keine Vorgängerknoten
			if(predecessors == null) break;
			//TODO Sortiere nach dem Gewicht der Kanten 
			Collections.sort(predecessors);
			v = predecessors.get(0);
		}
		//Den Anfangsknoten in den Pfad einfägen
		path.addVertex(start);
		//Pfad umkehren
		return path.reverse();
	}
	
	//Filtert Closed Liste nach einem Wert
		public List<GraphVertex> filterByValue(GraphVertex vertex, Map<GraphVertex, Integer> map, Integer value){
			if(value == null) return null;
			List<GraphVertex> filteredVertices = new ArrayList<GraphVertex>();
			for(GraphVertex v : map.keySet()){
				if(map.get(v).equals(value) && Graphs.neighborListOf(this, vertex).contains(v)) filteredVertices.add(v);
			}
			return filteredVertices;
		}
	
	protected List<GraphVertex> getSuccessors(GraphVertex vertex){
		countAccess();
		return Graphs.neighborListOf(this, vertex);
	}
	
	//Ermittelt Nachbarknoten
	public List<GraphVertex> getNeighbours(GraphVertex vertex, Map<GraphVertex, Integer> closed){
		List<GraphVertex> neighbours = new ArrayList<GraphVertex>();
		neighbours = getSuccessors(vertex);
		neighbours = new ArrayList(new HashSet(neighbours));
		//TODO Sortiere nach dem Gewicht der Kanten
		Collections.sort(neighbours);
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

