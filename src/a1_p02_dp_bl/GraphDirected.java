package a1_p02_dp_bl;

import java.io.*;
import java.text.ParseException;
import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;


//es muss ein Pseudograph als Basisklasse sein, weil sonst keine Schleifen erlaubt sind (graph1.gka enthält eine Schleife)
public class GraphDirected extends DirectedPseudograph<GraphVertex, DefaultWeightedEdge> implements GraphSerialization, GraphCommon<GraphVertex, DefaultWeightedEdge>
{
	private static final long serialVersionUID = -2586742127931932763L;
	boolean m_hasWeights = false;
	boolean m_hasAttributes = false;
	
	public GraphDirected(Class<? extends DefaultWeightedEdge> edgeClass) {
		super(edgeClass);
		// TODO Auto-generated constructor stub
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
				writer.write(Integer.toString(getEdgeSource(edge).getNumericAttribute()));
			}
			
			writer.write(",");
			writer.write(getEdgeTarget(edge).getName());
			if (m_hasAttributes) {
				writer.write(",");
				writer.write(Integer.toString(getEdgeTarget(edge).getNumericAttribute()));
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
	
	public void deserialize(Reader in) throws IOException, ParseException {
		String line;
		boolean inHeader = true;
		//boolean dirSpecified = false;
		LineNumberReader lineReader =  new LineNumberReader(in);
		lineReader.setLineNumber(0);
		
		// Graph erst leeren (Alle evtl. vorhandenen Edges und Vertices löschen )
		// in zwei schritten, da iterieren auf dem Set während des löschens Fehler verursacht
		DefaultWeightedEdge[] edges = edgeSet().toArray(new DefaultWeightedEdge[0]);
		removeAllEdges(edges);		
		Object[] vertices = vertexSet().toArray();
		for(Object v : vertices) {
			removeVertex((GraphVertex)v);
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
							//dirSpecified = true;
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
					
					GraphVertex vsrc = null;
					GraphVertex vdst = null;
					double weight = 0.0;
					if (!m_hasWeights && !m_hasAttributes) {
						// Wir erwarten genau 2 parameter
						if (kantenParameter.length != 2) {
							throw new ParseException("Line #" + lineReader.getLineNumber() +" : Invalid edge definition.", lineReader.getLineNumber());
						}
						vsrc = new GraphVertex(kantenParameter[0].trim());
						vdst = new GraphVertex(kantenParameter[1].trim());
					} else if (m_hasWeights && !m_hasAttributes) {
						// Gewichtet
						// Wir erwarten genau 3 parameter
						if (kantenParameter.length != 3) {
							throw new ParseException("Line #" + lineReader.getLineNumber() +" : Invalid edge definition.", lineReader.getLineNumber());
						}
						vsrc = new GraphVertex(kantenParameter[0].trim());
						vdst = new GraphVertex(kantenParameter[1].trim());
						weight =  Double.parseDouble(kantenParameter[2].trim());
					} else if (!m_hasWeights && m_hasAttributes) {
						// Attributiert
						// Wir erwarten genau 4 parameter
						if (kantenParameter.length != 4) {
							throw new ParseException("Line #" + lineReader.getLineNumber() +" : Invalid edge definition.", lineReader.getLineNumber());
						}
						vsrc = new GraphVertex(kantenParameter[0].trim(), Integer.parseInt(kantenParameter[1].trim()));
						vdst = new GraphVertex(kantenParameter[2].trim(), Integer.parseInt(kantenParameter[3].trim()));
					} else {
						// Attributiert und gewichtet
						// Wir erwarten genau 5 parameter
						if (kantenParameter.length != 5) {
							throw new ParseException("Line #" + lineReader.getLineNumber() +" : Invalid edge definition.", lineReader.getLineNumber());
						}
						vsrc = new GraphVertex(kantenParameter[0].trim(), Integer.parseInt(kantenParameter[1].trim()));
						vdst = new GraphVertex(kantenParameter[2].trim(), Integer.parseInt(kantenParameter[3].trim()));
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

	public void load(String filename) {
		try {
			deserialize(new FileReader(filename));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void clearAttributes(){
		for(GraphVertex v : vertexSet()){
			v.setNumericAttribute(0);
		}
	}
		
	
	public List<GraphVertex> getSuccessors(GraphVertex vertex) {
		return Graphs.successorListOf(this, vertex);
	}

	public List<GraphVertex> getPredecessors(GraphVertex vertex) {
		return Graphs.predecessorListOf(this, vertex);
	}

	@Override
	public boolean isDirected() {
		return true;
	}

	@Override
	public boolean hasEdgeWeights() {
		return m_hasWeights;
	}

	@Override
	public boolean hasVertexAttributes() {
		return m_hasAttributes;
	}
	

}




